import { useEffect, useState } from "react";

import {
    listarCondutores,
    type Condutor,
} from "../services/condutorService";

import {
    listarVeiculos,
    type Veiculo,
} from "../services/veiculoService";

import {
    listarTiposInfracao,
    type TipoInfracao,
} from "../services/tipoInfracaoService";

import {
    listarInfracoes,
    cadastrarInfracao,
    atualizarInfracao,
    excluirInfracao,
    type Infracao,
    type InfracaoRequest,
} from "../services/infracaoService";

import "./Infracoes.css";

function Infracoes() {
    const [infracoes, setInfracoes] = useState<Infracao[]>([]);
    const [condutores, setCondutores] = useState<Condutor[]>([]);
    const [veiculos, setVeiculos] = useState<Veiculo[]>([]);
    const [tiposInfracao, setTiposInfracao] = useState<TipoInfracao[]>([]);

    const [condutorId, setCondutorId] = useState("");
    const [veiculoId, setVeiculoId] = useState("");
    const [tipoInfracaoId, setTipoInfracaoId] = useState("");
    const [dataHora, setDataHora] = useState("");

    const [editandoId, setEditandoId] = useState<number | null>(null);

    const [mensagemSucesso, setMensagemSucesso] = useState("");
    const [mensagemErro, setMensagemErro] = useState("");

    const [carregando, setCarregando] = useState(true);

    useEffect(() => {
        carregarDados();
    }, []);

    async function carregarDados() {
        try {
            setCarregando(true);
            setMensagemErro("");

            const [
                infracoesData,
                condutoresData,
                veiculosData,
                tiposData,
            ] = await Promise.all([
                listarInfracoes(),
                listarCondutores(),
                listarVeiculos(),
                listarTiposInfracao(),
            ]);

            setInfracoes(infracoesData);
            setCondutores(condutoresData);
            setVeiculos(veiculosData);
            setTiposInfracao(tiposData);
        } catch (error) {
            console.error(error);
            setMensagemErro(
                "Não foi possível carregar os dados da página."
            );
        } finally {
            setCarregando(false);
        }
    }

    function limparFormulario() {
        setCondutorId("");
        setVeiculoId("");
        setTipoInfracaoId("");
        setDataHora("");
        setEditandoId(null);
    }

    function exibirErro(error: unknown) {
        console.error(error);

        const resposta = (
            error as {
                response?: {
                    data?: {
                        message?: string;
                    };
                };
            }
        )?.response?.data?.message;

        setMensagemErro(
            resposta || "Não foi possível realizar a operação."
        );
    }

    async function handleSubmit(
        event: React.FormEvent<HTMLFormElement>
    ) {
        event.preventDefault();

        setMensagemSucesso("");
        setMensagemErro("");

        if (!condutorId || !veiculoId || !tipoInfracaoId || !dataHora) {
            setMensagemErro(
                "Preencha todos os campos obrigatórios."
            );
            return;
        }

        const dados: InfracaoRequest = {
            condutorId: Number(condutorId),
            veiculoId: Number(veiculoId),
            tipoInfracaoId: Number(tipoInfracaoId),
            dataHora: dataHora,
        };

        try {
            if (editandoId !== null) {
                await atualizarInfracao(editandoId, dados);

                setMensagemSucesso(
                    "Infração atualizada com sucesso!"
                );
            } else {
                await cadastrarInfracao(dados);

                setMensagemSucesso(
                    "Infração registrada com sucesso!"
                );
            }

            limparFormulario();
            await carregarDados();
        } catch (error) {
            exibirErro(error);
        }
    }

    function iniciarEdicao(infracao: Infracao) {
        setMensagemSucesso("");
        setMensagemErro("");

        setEditandoId(infracao.id);
        setCondutorId(String(infracao.condutorId));
        setVeiculoId(String(infracao.veiculoId));
        setTipoInfracaoId(String(infracao.tipoInfracaoId));

        setDataHora(
            infracao.dataHora
                ? infracao.dataHora.substring(0, 16)
                : ""
        );

        window.scrollTo({
            top: 0,
            behavior: "smooth",
        });
    }

    async function handleExcluir(id: number) {
        const confirmar = window.confirm(
            "Tem certeza que deseja excluir esta infração?"
        );

        if (!confirmar) {
            return;
        }

        setMensagemSucesso("");
        setMensagemErro("");

        try {
            await excluirInfracao(id);

            setMensagemSucesso(
                "Infração excluída com sucesso!"
            );

            if (editandoId === id) {
                limparFormulario();
            }

            await carregarDados();
        } catch (error) {
            exibirErro(error);
        }
    }

    function formatarDataHora(data: string) {
        if (!data) {
            return "-";
        }

        const dataObj = new Date(data);

        if (Number.isNaN(dataObj.getTime())) {
            return data;
        }

        return dataObj.toLocaleString("pt-BR");
    }

    function formatarValor(valor: number) {
        return Number(valor).toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL",
        });
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

    /*
     * Mostra somente os veículos pertencentes ao condutor
     * selecionado.
     */
    const veiculosDoCondutor = veiculos.filter(
        (veiculo) =>
            String(veiculo.condutorId) === condutorId
    );

    return (
        <div className="page">
            <div className="page-header">
                <div>
                    <h1>Infrações</h1>
                    <p>
                        Registre e consulte as infrações de trânsito.
                    </p>
                </div>
            </div>

            {mensagemSucesso && (
                <div className="success-message">
                    {mensagemSucesso}
                </div>
            )}

            {mensagemErro && (
                <div className="error-message">
                    {mensagemErro}
                </div>
            )}

            <div className="form-card">
                <h2>
                    {editandoId !== null
                        ? "Editar Infração"
                        : "Registrar Infração"}
                </h2>

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="condutor">
                            Condutor
                        </label>

                        <select
                            id="condutor"
                            value={condutorId}
                            onChange={(event) => {
                                setCondutorId(event.target.value);
                                setVeiculoId("");
                            }}
                            required
                        >
                            <option value="">
                                Selecione um condutor
                            </option>

                            {condutores.map((condutor) => (
                                <option
                                    key={condutor.id}
                                    value={condutor.id}
                                >
                                    {condutor.nome} - CPF:{" "}
                                    {condutor.cpf}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="veiculo">
                            Veículo
                        </label>

                        <select
                            id="veiculo"
                            value={veiculoId}
                            onChange={(event) =>
                                setVeiculoId(
                                    event.target.value
                                )
                            }
                            disabled={!condutorId}
                            required
                        >
                            <option value="">
                                {condutorId
                                    ? "Selecione um veículo"
                                    : "Selecione primeiro o condutor"}
                            </option>

                            {veiculosDoCondutor.map(
                                (veiculo) => (
                                    <option
                                        key={veiculo.id}
                                        value={veiculo.id}
                                    >
                                        {veiculo.placa} -{" "}
                                        {veiculo.marca}{" "}
                                        {veiculo.modelo}
                                    </option>
                                )
                            )}
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="tipoInfracao">
                            Tipo de infração
                        </label>

                        <select
                            id="tipoInfracao"
                            value={tipoInfracaoId}
                            onChange={(event) =>
                                setTipoInfracaoId(
                                    event.target.value
                                )
                            }
                            required
                        >
                            <option value="">
                                Selecione o tipo de infração
                            </option>

                            {tiposInfracao.map(
                                (tipo) => (
                                    <option
                                        key={tipo.id}
                                        value={tipo.id}
                                    >
                                        {tipo.codigo} -{" "}
                                        {tipo.descricao}
                                    </option>
                                )
                            )}
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="dataHora">
                            Data e hora
                        </label>

                        <input
                            id="dataHora"
                            type="datetime-local"
                            value={dataHora}
                            onChange={(event) =>
                                setDataHora(
                                    event.target.value
                                )
                            }
                            required
                        />
                    </div>

                    <div className="form-actions">
                        <button
                            type="submit"
                            className="primary-button"
                        >
                            {editandoId !== null
                                ? "Salvar Alterações"
                                : "Registrar Infração"}
                        </button>

                        {editandoId !== null && (
                            <button
                                type="button"
                                className="cancel-button"
                                onClick={limparFormulario}
                            >
                                Cancelar
                            </button>
                        )}
                    </div>
                </form>
            </div>

            <div className="table-card">
                <div className="table-header">
                    <div>
                        <h2>Infrações registradas</h2>
                        <span>
                            {infracoes.length} infração
                            {infracoes.length !== 1
                                ? "ões"
                                : ""}
                        </span>
                    </div>
                </div>

                {carregando ? (
                    <p>Carregando infrações...</p>
                ) : infracoes.length === 0 ? (
                    <p>
                        Nenhuma infração cadastrada.
                    </p>
                ) : (
                    <div className="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Condutor</th>
                                    <th>Veículo</th>
                                    <th>Infração</th>
                                    <th>Gravidade</th>
                                    <th>Pontos</th>
                                    <th>Valor</th>
                                    <th>Data/Hora</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>

                            <tbody>
                                {infracoes.map(
                                    (infracao) => (
                                        <tr
                                            key={
                                                infracao.id
                                            }
                                        >
                                            <td>
                                                {
                                                    infracao.nomeCondutor
                                                }
                                            </td>

                                            <td>
                                                {
                                                    infracao.placaVeiculo
                                                }
                                            </td>

                                            <td>
                                                <strong>
                                                    {
                                                        infracao.codigoInfracao
                                                    }
                                                </strong>
                                                <br />
                                                {
                                                    infracao.descricaoInfracao
                                                }
                                            </td>

                                            <td>
                                                {formatarGravidade(
                                                    infracao.gravidade
                                                )}
                                            </td>

                                            <td>
                                                {
                                                    infracao.pontos
                                                }
                                            </td>

                                            <td>
                                                {formatarValor(
                                                    infracao.valor
                                                )}
                                            </td>

                                            <td>
                                                {formatarDataHora(
                                                    infracao.dataHora
                                                )}
                                            </td>

                                            <td>
                                                <div className="table-actions">
                                                    <button
                                                        type="button"
                                                        className="edit-button"
                                                        onClick={() =>
                                                            iniciarEdicao(
                                                                infracao
                                                            )
                                                        }
                                                    >
                                                        Editar
                                                    </button>

                                                    <button
                                                        type="button"
                                                        className="delete-button"
                                                        onClick={() =>
                                                            handleExcluir(
                                                                infracao.id
                                                            )
                                                        }
                                                    >
                                                        Excluir
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    )
                                )}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Infracoes;