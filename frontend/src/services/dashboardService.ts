import { api } from "./api";

export async function buscarResumoDashboard() {
    const [condutores, veiculos, tiposInfracao, infracoes] =
        await Promise.all([
            api.get("/condutores"),
            api.get("/veiculos"),
            api.get("/tipos-infracao"),
            api.get("/infracoes"),
        ]);

    return {
        totalCondutores: condutores.data.length,
        totalVeiculos: veiculos.data.length,
        totalTiposInfracao: tiposInfracao.data.length,
        totalInfracoes: infracoes.data.length,
    };
}