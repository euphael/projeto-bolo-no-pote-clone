import Footer from "./components/Footer";
import Header from "./components/Header";
import Home from "./components/Home"
import Login from "./components/Login";
import { BrowserRouter, Route, Routes } from "react-router-dom"
import Produtos from "./components/Products";
import 'react-toastify/dist/ReactToastify.css';
import Catalogo from "./components/Catalog";
import 'bootstrap/dist/css/bootstrap.min.css';
import Clientes from "./components/Clients";
import EditarPerfil from "./components/Profile";
import Checkout from "./components/Checkout";
import Orders from "./components/Orders";
import Pedidos from "./components/OrderManager";

const App = () => {
  return <div>
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login/*" element={<Login />} />
        <Route path="/produtos" element={<Produtos />} />
        <Route path="/gerenciarPedidos" element={<Pedidos />} />
        <Route path="/catalogo" element={<Catalogo />} />
        <Route path="/clientes" element={<Clientes />} />
        <Route path="/meuperfil" element={<EditarPerfil />} />
        <Route path="/meuspedidos" element={<Orders />} />
        <Route path="/checkout" element={<Checkout />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  </div>;
};

export default App;
