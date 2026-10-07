import { useEffect, useState } from "react";
import type { FormEvent } from "react";

import {
  cadastrarCondutor,
  listarCondutores,
  atualizarCondutor,
} from "../services/condutorService";

import type { Condutor } from "../services/condutorService";
import "./Condutores.css";

export default function Condutores() {
  const [condutores, setCondutores] = useState<Condutor[]>([]);

  const [nome, setNome] = useState("");
  const [cpf, setCpf] = useState("");
  const [numeroCnh, setNumeroCnh] = useState("");

  const [editandoId, setEditandoId] = useState<number | null>(null);

  const [carregando, setCarregando] = useState(true);
  const [salvando, setSalvando] = useState(false);
  const [mensagem, setMensagem] = useState("");
  const [erro, setErro] = useState("");

  async function carregarCondutores() {
  try {
    setCarregando(true);

    const dados = await listarCondutores();
    setCondutores(dados);
  } catch (error: any) {
    console.error(error);

    setErro(
      error.response?.data?.message ||
        "Não foi possível carregar os condutores."
    );
  } finally {
    setCarregando(false);
  }
}

  useEffect(() => {
    carregarCondutores();
  }, []);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();

    setMensagem("");
    setErro("");

    if (nome.trim().length < 3) {
      setErro("O nome deve possuir pelo menos 3 caracteres.");
      return;
    }

    if (cpf.length !== 11) {
      setErro("O CPF deve possuir 11 caracteres.");
      return;
    }

    if (numeroCnh.length !== 11) {
      setErro("O número da CNH deve possuir 11 caracteres.");
      return;
    }

    try {
      setSalvando(true);

      if (editandoId !== null) {
        await atualizarCondutor(editandoId, {
          nome,
          cpf,
          numeroCnh,
        });

        setMensagem("Condutor atualizado com sucesso!");
        setEditandoId(null);
      } else {
        await cadastrarCondutor({
          nome,
          cpf,
          numeroCnh,
        });

        setMensagem("Condutor cadastrado com sucesso!");
      }

      setNome("");
      setCpf("");
      setNumeroCnh("");

      await carregarCondutores();
    } catch (error: any) {
      console.error(error);

      setErro(
        error.response?.data?.message ||
          (editandoId !== null
            ? "Não foi possível atualizar o condutor."
            : "Não foi possível cadastrar o condutor.")
      );
    } finally {
      setSalvando(false);
    }
  }

  function handleEditar(condutor: Condutor) {
    setEditandoId(condutor.id);

    setNome(condutor.nome);
    setCpf(condutor.cpf);
    setNumeroCnh(condutor.numeroCnh);

    setMensagem("");
    setErro("");
  }

  function handleCancelarEdicao() {
    setEditandoId(null);

    setNome("");
    setCpf("");
    setNumeroCnh("");

    setMensagem("");
    setErro("");
  }

  return (
    <div className="condutores-page">
      <div className="page-header">
        <div>
          <h1>Condutores</h1>
          <p>Cadastro e consulta dos condutores do sistema.</p>
        </div>
      </div>

      <div className="condutores-content">
        <section className="form-card">
          <h2>
            {editandoId !== null ? "Editar condutor" : "Novo condutor"}
          </h2>

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="nome">Nome</label>

              <input
                id="nome"
                type="text"
                value={nome}
                onChange={(event) => setNome(event.target.value)}
                placeholder="Digite o nome completo"
              />
            </div>

            <div className="form-group">
              <label htmlFor="cpf">CPF</label>

              <input
                id="cpf"
                type="text"
                maxLength={11}
                value={cpf}
                onChange={(event) =>
                  setCpf(event.target.value.replace(/\D/g, ""))
                }
                placeholder="Digite o CPF"
              />
            </div>

            <div className="form-group">
              <label htmlFor="numeroCnh">Número da CNH</label>

              <input
                id="numeroCnh"
                type="text"
                maxLength={11}
                value={numeroCnh}
                onChange={(event) =>
                  setNumeroCnh(event.target.value.replace(/\D/g, ""))
                }
                placeholder="Digite o número da CNH"
              />
            </div>

            {mensagem && (
              <div className="success-message">
                {mensagem}
              </div>
            )}

            {erro && (
              <div className="error-message">
                {erro}
              </div>
            )}

            <button type="submit" disabled={salvando}>
              {salvando
                ? "Salvando..."
                : editandoId !== null
                  ? "Atualizar condutor"
                  : "Cadastrar condutor"}
            </button>

            {editandoId !== null && (
              <button
                type="button"
                onClick={handleCancelarEdicao}
                disabled={salvando}
              >
                Cancelar edição
              </button>
            )}
          </form>
        </section>

        <section className="table-card">
          <div className="table-header">
            <h2>Condutores cadastrados</h2>

            <span>
              {condutores.length} registro(s)
            </span>
          </div>

          {carregando ? (
            <p className="loading">
              Carregando condutores...
            </p>
          ) : condutores.length === 0 ? (
            <p className="empty">
              Nenhum condutor cadastrado.
            </p>
          ) : (
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>CNH</th>
                    <th>Pontos</th>
                    <th>Ações</th>
                  </tr>
                </thead>

                <tbody>
                  {condutores.map((condutor) => (
                    <tr key={condutor.id}>
                      <td>{condutor.id}</td>

                      <td>{condutor.nome}</td>

                      <td>{condutor.cpf}</td>

                      <td>{condutor.numeroCnh}</td>

                      <td>{condutor.pontuacaoCnh}</td>

                      <td>
                        <button
                          type="button"
                          onClick={() => handleEditar(condutor)}
                        >
                          Editar
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </div>
    </div>
  );
}