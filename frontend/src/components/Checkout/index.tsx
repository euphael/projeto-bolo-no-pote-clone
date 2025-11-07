import { useEffect, useState } from "react";
import Container from '@mui/material/Container';
import { Typography } from "@mui/material";
import Divider from '@mui/material/Divider';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import axios from "axios";
import { CartProducts } from "../../interfaces/CartProducts";
import Button from '@mui/material/Button';
import Avatar from '@mui/material/Avatar';
import { Pedido } from "../../interfaces/Pedido";
import { ProductPedido } from "../../interfaces/ProductPedido";
import { initMercadoPago, Payment } from '@mercadopago/sdk-react';
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";



export default function Checkout() {
  const [cartProducts, setCartProducts] = useState<CartProducts>();
  const [usrLoginid, setUsrLoginId] = useState(0);
  const [totalGeral, setTotalPrice] = useState<Number>(0);
  const [pedido, setPedido] = useState<Pedido>();
  const [produtosPedido, setProdutosPedido] = useState<ProductPedido>({ productId: 0, quantity: 0 });
  const [mostraCheckout, setMostraCheckout] = useState('block');
  const [mostraOpcoesPagamento, setMostraOpcoesPagamento] = useState('none');
  let navigate  = useNavigate();

  useEffect(() => {
    const cart = localStorage.getItem("cart");
    const login = localStorage.getItem("login");

    if (typeof cart == 'string') {
      setCartProducts(JSON.parse(cart));
    }

    if (typeof login == 'string') {
      setUsrLoginId(JSON.parse(login).id);
    };


  }, [])



  useEffect(() => {
    cartProducts?.forEach((element) => {
      setTotalPrice(Number(totalGeral) + Number(element.total));
      setProdutosPedido({ productId: element.product.id, quantity: element.quantity });
    })
  }, [cartProducts])

  useEffect(() => {
    setPedido({ product: produtosPedido, userId: usrLoginid, endDate: '10-05-2024' });
  }, [produtosPedido])


  const processCheckout = () => {
    initMercadoPago(`${import.meta.env.VITE_API_MP_KEY}`);
    setMostraCheckout('none');
    setMostraOpcoesPagamento('block');
    const login = localStorage.getItem("login");

    if (typeof login !== 'string') {
      console.log("Faça login primeiro!")
      return false
    };

    setUsrLoginId(JSON.parse(login).id);
  }


  return (
    <Container maxWidth="md" className="mt-5">
      <div style={{ display: mostraCheckout }}>
        <Typography style={{ fontSize: '3rem', fontWeight: 400 }}>Finalizar pedido</Typography>
        <Divider />
        <TableContainer style={{ display: mostraCheckout }}>
          <Table sx={{ minWidth: 350 }} style={{ marginTop: '2rem' }}>
            <TableHead style={{ backgroundColor: '#1976D2', color: '#FFFFFF', borderRadius: '25px' }}>
              <TableRow>
                <TableCell>Produto</TableCell>
                <TableCell align="left">Preço</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {cartProducts !== undefined ? cartProducts.map((row) => (
                <TableRow key={row.product.id}>
                  <TableCell component="th" scope="row">
                    <div style={{ display: 'flex', alignItems: 'center' }}>
                      <Avatar src={'data:image/png;base64,' + row.product.image} style={{ marginRight: '1rem' }}></Avatar>
                      <Typography>{row.product.name}</Typography>
                    </div>
                  </TableCell>
                  <TableCell align="left"> R$ {`${row.total}`}</TableCell>
                </TableRow>
              )) : <Typography>Não há produtos no seu carrinho!</Typography>}
            </TableBody>
          </Table>
        </TableContainer>
        <div style={{ justifyContent: 'end', display: 'flex' }}>
          <Typography style={{ fontSize: '2rem', fontWeight: '700', marginTop: '1rem' }}>Total: R$ {`${totalGeral}`}</Typography>
        </div>
        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
          <Button onClick={processCheckout} variant="contained" style={{ backgroundColor: '#019934', marginTop: '2rem', fontSize: '1.2rem' }}>Prosseguir para Pagamento</Button>
        </div>
      </div>
      <div style={{ display: mostraOpcoesPagamento }}>
        <Typography style={{ fontSize: '3rem', fontWeight: 400 }}>Realizar pagamento</Typography>
        <Payment
          initialization={{
            amount: 1,
          }}
          customization={{
            visual: {
              style: {
                theme: "default"
              }
            },
            paymentMethods: {
              creditCard: "all",
            }
          }}
          onSubmit={async (param) => {
            axios({
              method: 'post',
              url: `${import.meta.env.VITE_API_BASE_URL}/mp/card`,
              withCredentials: false,
              data: param.formData
            }).catch(function (error) {
              console.log(error);
            })
              .then(() => {
                axios({
                  method: 'post',
                  url: `${import.meta.env.VITE_API_BASE_URL}/order/create`,
                  withCredentials: false,
                  data: pedido
                }).catch(function (error){
                  console.log(error);
                })
                .then(()=> {
                    Swal.fire("Pedido confirmado com sucesso!", "", "success");
                    localStorage.removeItem("cart")
                    navigate('/meuspedidos')
            
                })
              })
          }}
        />

      </div>
    </Container>
  )





}