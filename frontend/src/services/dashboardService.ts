import { api } from "./api";

export interface DashboardResumo {
    condutores: number;
    veiculos: number;
    tiposInfracao: number;
    infracoes: number;
}

export async function buscarResumoDashboard(): Promise<DashboardResumo> {
    const [condutores, veiculos, tiposInfracao, infracoes] =
        await Promise.all([
            api.get("/condutores"),
            api.get("/veiculos"),
            api.get("/tipos-infracao"),
            api.get("/infracoes"),
        ]);

    return {
        condutores: condutores.data.length,
        veiculos: veiculos.data.length,
        tiposInfracao: tiposInfracao.data.length,
        infracoes: infracoes.data.length,
    };
}