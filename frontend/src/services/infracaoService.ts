import { api } from "./api";

export interface Infracao {
    id: number;

    condutorId: number;
    nomeCondutor: string;

    veiculoId: number;
    placaVeiculo: string;

    tipoInfracaoId: number;
    codigoInfracao: string;
    descricaoInfracao: string;

    gravidade: "LEVE" | "MEDIA" | "GRAVE" | "GRAVISSIMA";
    pontos: number;
    valor: number;

    dataHora: string;
}

export interface InfracaoRequest {
    condutorId: number;
    veiculoId: number;
    tipoInfracaoId: number;
    dataHora: string;
}

export async function listarInfracoes(): Promise<Infracao[]> {
    const response = await api.get("/infracoes");
    return response.data;
}

export async function cadastrarInfracao(
    infracao: InfracaoRequest
): Promise<Infracao> {
    const response = await api.post("/infracoes", infracao);
    return response.data;
}

export async function atualizarInfracao(
    id: number,
    infracao: InfracaoRequest
): Promise<Infracao> {
    const response = await api.put(`/infracoes/${id}`, infracao);
    return response.data;
}

export async function excluirInfracao(id: number): Promise<void> {
    await api.delete(`/infracoes/${id}`);
}

export async function listarInfracoesPorCondutor(
    condutorId: number
): Promise<Infracao[]> {
    const response = await api.get(
        `/infracoes/condutor/${condutorId}`
    );

    return response.data;
}

export async function listarInfracoesPorVeiculo(
    veiculoId: number
): Promise<Infracao[]> {
    const response = await api.get(
        `/infracoes/veiculo/${veiculoId}`
    );

    return response.data;
}