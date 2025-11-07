import { useEffect, useState } from "react"
import axios from "axios";
import Container from '@mui/material/Container';
import { Typography } from "@mui/material";
import Divider from '@mui/material/Divider';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Swal from 'sweetalert2';
import { Pedidos } from "../../interfaces/Pedidos";
import { Pedido } from "../../interfaces/Pedido";
import useWebSocket from "react-use-websocket";

export default function Orders() {
  const [userId, setUser] = useState(0);
  const [pedidos, setPedidos] = useState<Pedidos>([]);

  useWebSocket("ws://localhost:8080/websocket", {
    onMessage: (event) => {
      const updatedOrder: Pedido = JSON.parse(event.data);
      const ordersWithoutUpdatedOrder: Pedido[] = pedidos.filter(function (order: Pedido) {
        return order.id != updatedOrder.id;
      });
      if (ordersWithoutUpdatedOrder.length != pedidos.length) {
        setPedidos([...ordersWithoutUpdatedOrder, updatedOrder]);
      }
    }
  });

  const avaliarPedido = async (orderId: Number, userId: Number) => {
    const { value: formsValue } = await Swal.fire({
      title: "Avaliar pedido",
      html: `
        <input id="swal-input1" class="swal2-input" placeholder="Informe uma nota de 0 a 10">
        <input id="swal-input2" class="swal2-input" placeholder="Deixe um comentário">
      `,
      focusConfirm: false,
      preConfirm: () => {
        let grade = (document.getElementById('swal-input1') as HTMLInputElement).value;
        let comment = (document.getElementById('swal-input2') as HTMLInputElement).value;
        let gradeNumber: number;

        if (!grade || !comment) {
          Swal.showValidationMessage('Preencha todos os campos');
          return [];
        }

        try {
          gradeNumber = parseInt(grade);
        } catch (error) {
          Swal.showValidationMessage('Nota inválida');
          return [];
        }

        if (gradeNumber < 0 || gradeNumber > 10) {
          Swal.showValidationMessage('Nota inválida');
          return [];
        }
        return [
          gradeNumber,
          comment
        ]
      },
    });

    if (formsValue) {
      let data = JSON.stringify({
        orderId: orderId,
        userId: userId,
        grade: formsValue[0],
        comment: formsValue[1]
      });
      axios({
        method: "post",
        url: `${import.meta.env.VITE_API_BASE_URL}/feedback/create`,
        data: data,
        withCredentials: false,
        headers: {
          'Content-Type': 'application/json',
        }
      }).then((response) => {
        if (response.status === 201) {
          Swal.fire("Avaliação enviada com sucesso!", "", "success");
        } else {
          Swal.fire("Erro ao enviar avaliação", "", "error");
        }
      })
    }
  }


  const cancelaPedido = (id: Number) => {
    Swal.fire({
      title: "Deseja realmente cancelar seu pedido?",
      icon: "error",
      confirmButtonText: "Sim",
      cancelButtonText: "Não",
      showCancelButton: true,
      showCloseButton: true,

    }).then((result) => {
      if (result.isConfirmed) {
        axios({
          method: "get",
          url: `${import.meta.env.VITE_API_BASE_URL}/order/cancel?id=${id}`,
          withCredentials: false,
        }).then(() => {
          Swal.fire("Pedido cancelado com sucesso!", "", "success");
          window.location.reload();

        })

      } else if (result.isDenied) {
        Swal.fire("Changes are not saved", "", "info");
      }
    })
  }

  useEffect(() => {
    const userData = localStorage.getItem("login");
    if (typeof userData == 'string') {
      setUser(JSON.parse(userData).id);
    }
    if (userId !== 0 && userId !== undefined) {
      axios.get(`${import.meta.env.VITE_API_BASE_URL}/order/getAll?userId=${userId}`).then((response) => {
        setPedidos(response.data);
      })
    }
  }, [userId])



  const realizarNovoPedido = (pedido: Pedido) => {

    Swal.fire({
      title: "Deseja confirmar o pedido?",
      showDenyButton: true,
      confirmButtonText: "Confirmar",
      denyButtonText: `Não`
    }).then((result) => {
      if (result.isConfirmed) {
        axios({
          method: 'post',
          url: `${import.meta.env.VITE_API_BASE_URL}/order/create`,
          withCredentials: false,
          data: {
            "product": {
              "productId": pedido.product.id,
              "quantity": pedido.quantity

            },
            "userId": userId,
            "endDate": '10-05-2024'
          }
        }).catch(function (error) {
          console.log(error);
        })
          .then(() => {
            window.location.reload();
            Swal.fire("Pedido confirmado com sucesso!", "", "success");

          })

      } else if (result.isDenied) {
        return
      }
    });
  }


  return (
    <Container maxWidth="lg" className="mt-5">
      <Typography style={{ fontSize: '3rem', fontWeight: 400 }}>Meus pedidos</Typography>
      <Divider />
      <TableContainer>
        <Table sx={{ minWidth: 350 }} style={{ marginTop: '2rem' }}>
          <TableHead style={{ backgroundColor: '#1976D2', color: '#FFFFFF', borderRadius: '25px' }}>
            <TableRow>
              <TableCell align="left">Pedido realizado em</TableCell>
              <TableCell align="left">Produto</TableCell>
              <TableCell align="left">Status</TableCell>
              <TableCell align="left">Total</TableCell>
              <TableCell align="left">Enviar para</TableCell>
              <TableCell align="center">Ações</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {pedidos.map((row) => (

              <TableRow key={row.id}>
                <TableCell align="left"> {row.endDate}</TableCell>
                <TableCell component="th" scope="row">
                  <div style={{ display: 'flex', alignItems: 'center', flexDirection: 'row' }}>
                    <Avatar src={'data:image/png;base64,' + row.product.image} style={{ marginRight: '1rem' }}></Avatar>
                    <Typography>{row.product.name}</Typography>
                  </div>
                </TableCell>
                <TableCell align="left"> <b>{row.status}</b></TableCell>
                <TableCell align="left"> R$ {row.product.price}</TableCell>
                <TableCell align="left">Nome Usuário</TableCell>
                {row.status === 'ENVIADO' ?
                  <TableCell align="left">
                    <div style={{ display: 'flex', justifyContent: 'center', flexDirection: 'column', width: '100%' }}>
                      <Button variant="contained" onClick={() => { cancelaPedido(Number(row.id)) }} style={{ backgroundColor: "red" }}>Cancelar</Button>
                    </div>
                  </TableCell> : ''}
                {row.status === 'ENTREGUE' ?
                  <TableCell align="center">
                    <div style={{ display: 'flex', justifyContent: 'center', flexDirection: 'column', width: '100%' }}>
                      <Button variant="contained" onClick={() => { avaliarPedido(Number(row.id), row.userId) }} style={{ backgroundColor: "green" }}>Avaliar</Button>
                      <Button variant="text" onClick={() => realizarNovoPedido(row)} style={{ padding: '0', marginTop: '1rem' }}>Realizar Pedido Novamente</Button>
                    </div>
                  </TableCell> : ''}
                {row.status === 'ENTREGUE' ? <TableCell align="left"></TableCell> : ''}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>


  )

}
