import { useEffect, useState } from "react";
import type { FormEvent } from "react";

import {
    cadastrarVeiculo,
    listarVeiculos,
    atualizarVeiculo,
    excluirVeiculo,
} from "../services/veiculoService";

import type {
    Veiculo,
    VeiculoRequest,
} from "../services/veiculoService";

import {
    listarCondutores,
} from "../services/condutorService";

import type {
    Condutor,
} from "../services/condutorService";

import "./Veiculos.css";

export default function Veiculos() {
    const [veiculos, setVeiculos] = useState<Veiculo[]>([]);
    const [condutores, setCondutores] = useState<Condutor[]>([]);

    const [placa, setPlaca] = useState("");
    const [renavam, setRenavam] = useState("");
    const [marca, setMarca] = useState("");
    const [modelo, setModelo] = useState("");
    const [ano, setAno] = useState("");
    const [condutorId, setCondutorId] = useState("");

    const [editandoId, setEditandoId] = useState<number | null>(null);

    const [carregando, setCarregando] = useState(true);
    const [salvando, setSalvando] = useState(false);

    const [mensagem, setMensagem] = useState("");
    const [erro, setErro] = useState("");

    async function carregarDados() {
        try {
            setCarregando(true);

            const [veiculosData, condutoresData] = await Promise.all([
                listarVeiculos(),
                listarCondutores(),
            ]);

            setVeiculos(veiculosData);
            setCondutores(condutoresData);
        } catch (error) {
            console.error(error);
            setErro("Não foi possível carregar os dados.");
        } finally {
            setCarregando(false);
        }
    }

    useEffect(() => {
        carregarDados();
    }, []);

    function limparFormulario() {
        setPlaca("");
        setRenavam("");
        setMarca("");
        setModelo("");
        setAno("");
        setCondutorId("");
        setEditandoId(null);
    }

    function iniciarEdicao(veiculo: Veiculo) {
        setEditandoId(veiculo.id);

        setPlaca(veiculo.placa);
        setRenavam(veiculo.renavam);
        setMarca(veiculo.marca);
        setModelo(veiculo.modelo);
        setAno(String(veiculo.ano));
        setCondutorId(String(veiculo.condutorId));

        setMensagem("");
        setErro("");

        window.scrollTo({
            top: 0,
            behavior: "smooth",
        });
    }

    async function handleSubmit(event: FormEvent) {
        event.preventDefault();

        setMensagem("");
        setErro("");

        if (placa.trim().length !== 7) {
            setErro("A placa deve possuir 7 caracteres.");
            return;
        }

        if (!renavam.trim()) {
            setErro("O RENAVAM é obrigatório.");
            return;
        }

        if (!marca.trim()) {
            setErro("A marca é obrigatória.");
            return;
        }

        if (!modelo.trim()) {
            setErro("O modelo é obrigatório.");
            return;
        }

        if (!ano) {
            setErro("O ano é obrigatório.");
            return;
        }

        if (!condutorId) {
            setErro("Selecione um condutor.");
            return;
        }

        const dados: VeiculoRequest = {
            placa: placa.trim().toUpperCase(),
            renavam: renavam.trim(),
            marca: marca.trim(),
            modelo: modelo.trim(),
            ano: Number(ano),
            condutorId: Number(condutorId),
        };

        try {
            setSalvando(true);

            if (editandoId !== null) {
                await atualizarVeiculo(editandoId, dados);

                setMensagem("Veículo atualizado com sucesso!");
            } else {
                await cadastrarVeiculo(dados);

                setMensagem("Veículo cadastrado com sucesso!");
            }

            limparFormulario();

            await carregarDados();
        } catch (error) {
            console.error(error);

            setErro(
                editandoId !== null
                    ? "Não foi possível atualizar o veículo."
                    : "Não foi possível cadastrar o veículo."
            );
        } finally {
            setSalvando(false);
        }
    }

    async function handleExcluir(id: number) {
        const confirmar = window.confirm(
            "Tem certeza que deseja excluir este veículo?"
        );

        if (!confirmar) {
            return;
        }

        setMensagem("");
        setErro("");

        try {
            await excluirVeiculo(id);

            setMensagem("Veículo excluído com sucesso!");

            if (editandoId === id) {
                limparFormulario();
            }

            await carregarDados();
        } catch (error: any) {
            console.error(error);

            if (error.response?.status === 409) {
                setErro(
                    error.response.data ||
                    "Não é possível excluir este veículo porque existem infrações vinculadas a ele."
                );
            } else {
                setErro("Não foi possível excluir o veículo.");
            }
        }
    }

    return (
        <div className="veiculos-page">

            <div className="page-header">
                <div>
                    <h1>Veículos</h1>

                    <p>
                        Cadastro e consulta dos veículos do sistema.
                    </p>
                </div>
            </div>

            <div className="veiculos-content">

                <section className="form-card">

                    <h2>
                        {editandoId !== null
                            ? "Editar veículo"
                            : "Novo veículo"}
                    </h2>

                    <form onSubmit={handleSubmit}>

                        <div className="form-group">
                            <label htmlFor="placa">
                                Placa
                            </label>

                            <input
                                id="placa"
                                type="text"
                                maxLength={7}
                                value={placa}
                                onChange={(event) =>
                                    setPlaca(
                                        event.target.value
                                            .replace(/[^a-zA-Z0-9]/g, "")
                                            .toUpperCase()
                                    )
                                }
                                placeholder="ABC1D23"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="renavam">
                                RENAVAM
                            </label>

                            <input
                                id="renavam"
                                type="text"
                                value={renavam}
                                onChange={(event) =>
                                    setRenavam(
                                        event.target.value.replace(/\D/g, "")
                                    )
                                }
                                placeholder="Digite o RENAVAM"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="marca">
                                Marca
                            </label>

                            <input
                                id="marca"
                                type="text"
                                value={marca}
                                onChange={(event) =>
                                    setMarca(event.target.value)
                                }
                                placeholder="Ex.: Toyota"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="modelo">
                                Modelo
                            </label>

                            <input
                                id="modelo"
                                type="text"
                                value={modelo}
                                onChange={(event) =>
                                    setModelo(event.target.value)
                                }
                                placeholder="Ex.: Corolla"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="ano">
                                Ano
                            </label>

                            <input
                                id="ano"
                                type="number"
                                value={ano}
                                onChange={(event) =>
                                    setAno(event.target.value)
                                }
                                placeholder="Ex.: 2022"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="condutorId">
                                Condutor
                            </label>

                            <select
                                id="condutorId"
                                value={condutorId}
                                onChange={(event) =>
                                    setCondutorId(event.target.value)
                                }
                            >
                                <option value="">
                                    Selecione um condutor
                                </option>

                                {condutores.map((condutor) => (
                                    <option
                                        key={condutor.id}
                                        value={condutor.id}
                                    >
                                        {condutor.nome}
                                    </option>
                                ))}
                            </select>
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
                                    : "Cadastrar veículo"}
                        </button>

                        {editandoId !== null && (
                            <button
                                type="button"
                                className="cancel-button"
                                onClick={limparFormulario}
                            >
                                Cancelar edição
                            </button>
                        )}

                    </form>

                </section>

                <section className="table-card">

                    <div className="table-header">
                        <h2>Veículos cadastrados</h2>

                        <span>
                            {veiculos.length} registro(s)
                        </span>
                    </div>

                    {carregando ? (
                        <p className="loading">
                            Carregando veículos...
                        </p>
                    ) : veiculos.length === 0 ? (
                        <p className="empty">
                            Nenhum veículo cadastrado.
                        </p>
                    ) : (
                        <div className="table-container">

                            <table>

                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Placa</th>
                                        <th>RENAVAM</th>
                                        <th>Marca</th>
                                        <th>Modelo</th>
                                        <th>Ano</th>
                                        <th>Condutor</th>
                                        <th>Ações</th>
                                    </tr>
                                </thead>

                                <tbody>

                                    {veiculos.map((veiculo) => (
                                        <tr key={veiculo.id}>

                                            <td>
                                                {veiculo.id}
                                            </td>

                                            <td>
                                                {veiculo.placa}
                                            </td>

                                            <td>
                                                {veiculo.renavam}
                                            </td>

                                            <td>
                                                {veiculo.marca}
                                            </td>

                                            <td>
                                                {veiculo.modelo}
                                            </td>

                                            <td>
                                                {veiculo.ano}
                                            </td>

                                            <td>
                                                {veiculo.nomeCondutor}
                                            </td>

                                            <td>
                                                <div className="action-buttons">

                                                    <button
                                                        type="button"
                                                        className="edit-button"
                                                        onClick={() =>
                                                            iniciarEdicao(veiculo)
                                                        }
                                                    >
                                                        Editar
                                                    </button>

                                                    <button
                                                        type="button"
                                                        className="delete-button"
                                                        onClick={() =>
                                                            handleExcluir(veiculo.id)
                                                        }
                                                    >
                                                        Excluir
                                                    </button>

                                                </div>
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