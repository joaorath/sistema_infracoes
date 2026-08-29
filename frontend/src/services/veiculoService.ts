import { api } from "./api";

export interface Veiculo {
    id: number;
    placa: string;
    renavam: string;
    marca: string;
    modelo: string;
    ano: number;
    condutorId: number;
    nomeCondutor: string;
}

export interface VeiculoRequest {
    placa: string;
    renavam: string;
    marca: string;
    modelo: string;
    ano: number;
    condutorId: number;
}

export async function listarVeiculos(): Promise<Veiculo[]> {
    const response = await api.get("/veiculos");

    return response.data;
}

export async function cadastrarVeiculo(
    veiculo: VeiculoRequest
): Promise<Veiculo> {
    const response = await api.post("/veiculos", veiculo);

    return response.data;
}

export async function atualizarVeiculo(
    id: number,
    veiculo: VeiculoRequest
): Promise<Veiculo> {
    const response = await api.put(`/veiculos/${id}`, veiculo);

    return response.data;
}

export async function excluirVeiculo(id: number): Promise<void> {
    await api.delete(`/veiculos/${id}`);
}