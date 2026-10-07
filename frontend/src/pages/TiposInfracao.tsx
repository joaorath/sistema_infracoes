import { useEffect, useState } from "react";
import type { FormEvent } from "react";

import {
  cadastrarTipoInfracao,
  listarTiposInfracao,
  atualizarTipoInfracao,
  excluirTipoInfracao,
} from "../services/tipoInfracaoService";

import type {
  TipoInfracao,
  TipoInfracaoRequest,
} from "../services/tipoInfracaoService";

import "./TiposInfracao.css";

export default function TiposInfracao() {
  const [tipos, setTipos] = useState<TipoInfracao[]>([]);

  const [codigo, setCodigo] = useState("");
  const [descricao, setDescricao] = useState("");

  const [gravidade, setGravidade] =
    useState<TipoInfracaoRequest["gravidade"]>("LEVE");

  const [pontos, setPontos] = useState("");
  const [valor, setValor] = useState("");

  const [editandoId, setEditandoId] =
    useState<number | null>(null);

  const [carregando, setCarregando] = useState(true);
  const [salvando, setSalvando] = useState(false);

  const [mensagem, setMensagem] = useState("");
  const [erro, setErro] = useState("");

  async function carregarTipos() {
    try {
      setCarregando(true);

      const dados = await listarTiposInfracao();

      setTipos(dados);
    } catch (error: any) {
      console.error(error);

      setErro(
        error.response?.data?.message ||
          "Não foi possível carregar os tipos de infração."
      );
    } finally {
      setCarregando(false);
    }
  }

  useEffect(() => {
    carregarTipos();
  }, []);

  function limparFormulario() {
    setCodigo("");
    setDescricao("");
    setGravidade("LEVE");
    setPontos("");
    setValor("");
    setEditandoId(null);
  }

  function iniciarEdicao(tipo: TipoInfracao) {
    setEditandoId(tipo.id);

    setCodigo(tipo.codigo);
    setDescricao(tipo.descricao);
    setGravidade(tipo.gravidade);
    setPontos(String(tipo.pontos));
    setValor(String(tipo.valor));

    setMensagem("");
    setErro("");

    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  }

  async function handleExcluir(id: number) {
    const confirmar = window.confirm(
      "Tem certeza que deseja excluir este tipo de infração?"
    );

    if (!confirmar) {
      return;
    }

    setMensagem("");
    setErro("");

    try {
      await excluirTipoInfracao(id);

      setMensagem(
        "Tipo de infração excluído com sucesso!"
      );

      if (editandoId === id) {
        limparFormulario();
      }

      await carregarTipos();
    } catch (error: any) {
      console.error(error);

      setErro(
        error.response?.data?.message ||
          "Não foi possível excluir o tipo de infração."
      );
    }
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();

    setMensagem("");
    setErro("");

    if (codigo.trim().length < 3) {
      setErro(
        "O código deve possuir pelo menos 3 caracteres."
      );

      return;
    }

    if (descricao.trim().length < 5) {
      setErro(
        "A descrição deve possuir pelo menos 5 caracteres."
      );

      return;
    }

    if (!gravidade) {
      setErro("Selecione a gravidade.");
      return;
    }

    if (!pontos) {
      setErro("Informe a quantidade de pontos.");
      return;
    }

    if (Number(pontos) < 0) {
      setErro("Os pontos não podem ser negativos.");
      return;
    }

    if (!valor) {
      setErro("Informe o valor da multa.");
      return;
    }

    if (Number(valor) < 0) {
      setErro("O valor não pode ser negativo.");
      return;
    }

    const dados: TipoInfracaoRequest = {
      codigo: codigo.trim().toUpperCase(),
      descricao: descricao.trim(),
      gravidade,
      pontos: Number(pontos),
      valor: Number(valor),
    };

    try {
      setSalvando(true);

      if (editandoId !== null) {
        await atualizarTipoInfracao(
          editandoId,
          dados
        );

        setMensagem(
          "Tipo de infração atualizado com sucesso!"
        );
      } else {
        await cadastrarTipoInfracao(dados);

        setMensagem(
          "Tipo de infração cadastrado com sucesso!"
        );
      }

      limparFormulario();

      await carregarTipos();
    } catch (error: any) {
      console.error(error);

      setErro(
        error.response?.data?.message ||
          (editandoId !== null
            ? "Não foi possível atualizar o tipo de infração."
            : "Não foi possível cadastrar o tipo de infração.")
      );
    } finally {
      setSalvando(false);
    }
  }

  function formatarGravidade(gravidade: string) {
    switch (gravidade) {
      case "LEVE":
        return "Leve";

      case "MEDIA":
        return "Média";

      case "GRAVE":
        return "Grave";

      case "GRAVISSIMA":
        return "Gravíssima";

      default:
        return gravidade;
    }
  }

  function formatarValor(valor: number) {
    return Number(valor).toLocaleString("pt-BR", {
      style: "currency",
      currency: "BRL",
    });
  }

  return (
    <div className="tipos-infracao-page">
      <div className="page-header">
        <div>
          <h1>Tipos de Infração</h1>

          <p>
            Cadastro e consulta dos tipos de infrações de trânsito.
          </p>
        </div>
      </div>

      <div className="tipos-infracao-content">
        <section className="form-card">
          <h2>
            {editandoId !== null
              ? "Editar tipo de infração"
              : "Novo tipo de infração"}
          </h2>

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="codigo">
                Código
              </label>

              <input
                id="codigo"
                type="text"
                maxLength={20}
                value={codigo}
                onChange={(event) =>
                  setCodigo(event.target.value)
                }
                placeholder="Ex.: 005"
              />
            </div>

            <div className="form-group">
              <label htmlFor="descricao">
                Descrição
              </label>

              <input
                id="descricao"
                type="text"
                maxLength={255}
                value={descricao}
                onChange={(event) =>
                  setDescricao(event.target.value)
                }
                placeholder="Ex.: Dirigir sem habilitação"
              />
            </div>

            <div className="form-group">
              <label htmlFor="gravidade">
                Gravidade
              </label>

              <select
                id="gravidade"
                value={gravidade}
                onChange={(event) =>
                  setGravidade(
                    event.target
                      .value as TipoInfracaoRequest["gravidade"]
                  )
                }
              >
                <option value="LEVE">
                  Leve
                </option>

                <option value="MEDIA">
                  Média
                </option>

                <option value="GRAVE">
                  Grave
                </option>

                <option value="GRAVISSIMA">
                  Gravíssima
                </option>
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="pontos">
                Pontos
              </label>

              <input
                id="pontos"
                type="number"
                min="0"
                value={pontos}
                onChange={(event) =>
                  setPontos(event.target.value)
                }
                placeholder="Ex.: 5"
              />
            </div>

            <div className="form-group">
              <label htmlFor="valor">
                Valor da multa
              </label>

              <input
                id="valor"
                type="number"
                min="0"
                step="0.01"
                value={valor}
                onChange={(event) =>
                  setValor(event.target.value)
                }
                placeholder="Ex.: 195.23"
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

            <button
              type="submit"
              disabled={salvando}
            >
              {salvando
                ? "Salvando..."
                : editandoId !== null
                  ? "Salvar alterações"
                  : "Cadastrar tipo de infração"}
            </button>

            {editandoId !== null && (
              <button
                type="button"
                className="cancel-button"
                onClick={limparFormulario}
                disabled={salvando}
              >
                Cancelar edição
              </button>
            )}
          </form>
        </section>

        <section className="table-card">
          <div className="table-header">
            <h2>
              Tipos de infração cadastrados
            </h2>

            <span>
              {tipos.length} registro(s)
            </span>
          </div>

          {carregando ? (
            <p className="loading">
              Carregando tipos de infração...
            </p>
          ) : tipos.length === 0 ? (
            <p className="empty">
              Nenhum tipo de infração cadastrado.
            </p>
          ) : (
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Código</th>
                    <th>Descrição</th>
                    <th>Gravidade</th>
                    <th>Pontos</th>
                    <th>Valor</th>
                    <th>Ações</th>
                  </tr>
                </thead>

                <tbody>
                  {tipos.map((tipo) => (
                    <tr key={tipo.id}>
                      <td>{tipo.id}</td>

                      <td>{tipo.codigo}</td>

                      <td>{tipo.descricao}</td>

                      <td>
                        {formatarGravidade(
                          tipo.gravidade
                        )}
                      </td>

                      <td>{tipo.pontos}</td>

                      <td>
                        {formatarValor(tipo.valor)}
                      </td>

                      <td>
                        <button
                          type="button"
                          className="edit-button"
                          onClick={() =>
                            iniciarEdicao(tipo)
                          }
                        >
                          Editar
                        </button>

                        <button
                          type="button"
                          className="delete-button"
                          onClick={() =>
                            handleExcluir(tipo.id)
                          }
                        >
                          Excluir
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