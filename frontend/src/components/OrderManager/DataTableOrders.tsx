import * as React from 'react';
import { DataGrid, GridColDef, GridRowSelectionModel } from '@mui/x-data-grid';
import { useEffect, useState } from 'react';
import axios from "axios";
import DeleteIcon from '@mui/icons-material/Delete';
import IconButton from '@mui/material/IconButton';
import Button from '@mui/material/Button';
import Swal from "sweetalert2";
import { Pedido } from '../../interfaces/Pedido';

export default function DataTableOrders() {
  const [dataOrders, setData] = useState<Pedido[]>([]);
  const [rowSelectionModel, setRowSelectionModel] = React.useState<GridRowSelectionModel>([]);

  const columns: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'userName', headerName: 'Nome do Usuário', width: 200 },
    { field: 'productName', headerName: 'Nome do Produto', width: 200 },
    { field: 'quantity', headerName: 'Quantidade', width: 130 },
    { field: 'status', headerName: 'Status', width: 130 },
    { field: 'grade', headerName: 'Nota', width: 130 },
    { field: 'comment', headerName: 'Comentário', width: 130 },
    {
      field: 'actions',
      headerName: 'Ações',
      width: 200,
      sortable: false,
      disableColumnMenu: true,
      renderCell: (params) => (
        <div>
          <Button
            variant="contained"
            color="primary"
            onClick={() => handleUpdateStatus(params.row, 'Finalizado')}
            style={{ marginRight: 8 }}
          >
            Finalizado
          </Button>
          <Button
            variant="contained"
            color="secondary"
            onClick={() => handleUpdateStatus(params.row, 'Em andamento')}
          >
            Em andamento
          </Button>
        </div>
      ),
    },
    {
      field: "delete",
      width: 75,
      sortable: false,
      disableColumnMenu: true,
      renderHeader: () => {
        return (
          <IconButton
            onClick={() => {
              const selectedIDs = new Set(rowSelectionModel);
              setData((r) => r.filter((x:any) => !selectedIDs.has(x.id)));
            }}
          >
            <DeleteIcon />
          </IconButton>
        );
      }
    }
  ];

  const handleUpdateStatus = (pedido: Pedido, status: string) => {

    setData((prevData) =>
      prevData.map((order) =>
        order.id === pedido.id ? { ...order, status: status } : order
      )
    );

    axios({
      method: 'PUT',
      url: `${import.meta.env.VITE_API_BASE_URL}/order/update?id=${pedido.id}`,
      data: {
        "product": {
          "productId": pedido.id,
          "quantity": pedido.quantity
        },
        "userId": pedido.userId,
        "status": status,
        "endDate": pedido.endDate
      }
    }).then((response) => {
      if (response.data) {
        Swal.fire(`Pedido atualizado com sucesso. NOVO STATUS: ${status}`, "", "success");
      }
    });

  };

  useEffect(() => {
    axios({
      method: 'GET',
      url: `${import.meta.env.VITE_API_BASE_URL}/order/all`,
    }).then((response) => {
      if (response.data) {
        setData(response.data);
      }
    });
  }, []);

  return (
    <div style={{ height: '100%', width: '100%' }}>
      <DataGrid
        rows={dataOrders}
        columns={columns}
        initialState={{
          pagination: {
            paginationModel: { page: 0, pageSize: 8 },
          },
        }}
        pageSizeOptions={[5, 10]}
        checkboxSelection
        onRowSelectionModelChange={(newRowSelectionModel) => {
          setRowSelectionModel(newRowSelectionModel);
        }}
        style={{ fontSize: '1.4rem' }}
        rowSelectionModel={rowSelectionModel}
      />
    </div>
  );
}
