import { api } from "./api";

export interface TipoInfracao {
    id: number;
    codigo: string;
    descricao: string;
    gravidade: "LEVE" | "MEDIA" | "GRAVE" | "GRAVISSIMA";
    pontos: number;
    valor: number;
}

export interface TipoInfracaoRequest {
    codigo: string;
    descricao: string;
    gravidade: "LEVE" | "MEDIA" | "GRAVE" | "GRAVISSIMA";
    pontos: number;
    valor: number;
}

export async function listarTiposInfracao(): Promise<TipoInfracao[]> {
    const response = await api.get("/tipos-infracao");

    return response.data;
}

export async function cadastrarTipoInfracao(
    tipoInfracao: TipoInfracaoRequest
): Promise<TipoInfracao> {
    const response = await api.post(
        "/tipos-infracao",
        tipoInfracao
    );

    return response.data;
}

export async function atualizarTipoInfracao(
    id: number,
    tipoInfracao: TipoInfracaoRequest
): Promise<TipoInfracao> {
    const response = await api.put(
        `/tipos-infracao/${id}`,
        tipoInfracao
    );

    return response.data;
}

export async function excluirTipoInfracao(
    id: number
): Promise<void> {
    await api.delete(`/tipos-infracao/${id}`);
}