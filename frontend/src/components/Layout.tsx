import { Link, Outlet } from "react-router-dom";
import "./Layout.css";

function Layout() {
  return (
    <div className="app-layout">
      <aside className="sidebar">
        <h2>Sistema de Trânsito</h2>

        <nav>
          <Link to="/">Dashboard</Link>
          <Link to="/condutores">Condutores</Link>
          <Link to="/veiculos">Veículos</Link>
          <Link to="/tipos-infracao">Tipos de Infração</Link>
          <Link to="/infracoes">Infrações</Link>
        </nav>
      </aside>

      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
}

export default Layout;