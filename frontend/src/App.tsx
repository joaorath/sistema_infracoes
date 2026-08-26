import { BrowserRouter, Routes, Route } from "react-router-dom";

import Layout from "./components/Layout";

import Dashboard from "./pages/Dashboard";
import Condutores from "./pages/Condutores";
import Veiculos from "./pages/Veiculos";
import TiposInfracao from "./pages/TiposInfracao";
import Infracoes from "./pages/Infracoes";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route element={<Layout />}>
                    <Route path="/" element={<Dashboard />} />
                    <Route path="/condutores" element={<Condutores />} />
                    <Route path="/veiculos" element={<Veiculos />} />
                    <Route
                        path="/tipos-infracao"
                        element={<TiposInfracao />}
                    />
                    <Route path="/infracoes" element={<Infracoes />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;