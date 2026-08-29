import { api } from "./api";

export interface Condutor {
    id: number;
    nome: string;
    cpf: string;
    numeroCnh: string;
    pontuacaoCnh: number;
}

export interface CondutorRequest {
    nome: string;
    cpf: string;
    numeroCnh: string;
}

export async function listarCondutores(): Promise<Condutor[]> {
    const response = await api.get("/condutores");
    return response.data;
}

export async function cadastrarCondutor(
    condutor: CondutorRequest
): Promise<Condutor> {
    const response = await api.post("/condutores", condutor);
    return response.data;
}

export async function atualizarCondutor(
    id: number,
    condutor: CondutorRequest
): Promise<Condutor> {
    const response = await api.put(`/condutores/${id}`, condutor);
    return response.data;
}