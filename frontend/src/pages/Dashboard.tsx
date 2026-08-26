import { useEffect, useState } from "react";
import { buscarResumoDashboard } from "../services/dashboardService";
import "./Dashboard.css";

interface DashboardResumo {
  condutores: number;
  veiculos: number;
  tiposInfracao: number;
  infracoes: number;
}

export default function Dashboard() {
  const [resumo, setResumo] = useState<DashboardResumo>({
    condutores: 0,
    veiculos: 0,
    tiposInfracao: 0,
    infracoes: 0,
  });

  useEffect(() => {
    async function carregarDashboard() {
      try {
        const dados = await buscarResumoDashboard();
        setResumo(dados);
      } catch (error) {
        console.error("Erro ao carregar dashboard:", error);
      }
    }

    carregarDashboard();
  }, []);

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <div>
          <h1>Dashboard</h1>
          <p>Visão geral do Sistema de Gestão de Infrações de Trânsito</p>
        </div>
      </div>

      <div className="cards-grid">
        <div className="dashboard-card">
          <div className="card-icon">👤</div>
          <div>
            <span>Condutores</span>
            <strong>{resumo.condutores}</strong>
          </div>
        </div>

        <div className="dashboard-card">
          <div className="card-icon">🚗</div>
          <div>
            <span>Veículos</span>
            <strong>{resumo.veiculos}</strong>
          </div>
        </div>

        <div className="dashboard-card">
          <div className="card-icon">⚠️</div>
          <div>
            <span>Tipos de Infração</span>
            <strong>{resumo.tiposInfracao}</strong>
          </div>
        </div>

        <div className="dashboard-card">
          <div className="card-icon">📋</div>
          <div>
            <span>Infrações Registradas</span>
            <strong>{resumo.infracoes}</strong>
          </div>
        </div>
      </div>

      <div className="dashboard-section">
        <h2>Resumo do sistema</h2>

        <div className="summary-box">
          <p>
            O sistema possui atualmente{" "}
            <strong>{resumo.infracoes}</strong> infrações registradas
            envolvendo <strong>{resumo.veiculos}</strong> veículo(s) e{" "}
            <strong>{resumo.condutores}</strong> condutor(es).
          </p>
        </div>
      </div>
    </div>
  );
}