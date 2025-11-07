import * as React from 'react';
import { DataGrid, GridColDef, GridRowSelectionModel } from '@mui/x-data-grid';
import { useEffect, useState } from 'react'
import axios from "axios";
import DeleteIcon from '@mui/icons-material/Delete';
import IconButton from '@mui/material/IconButton';

export default function DataTable() {

  const [dataClients, setData] = useState([]);
  const [rowSelectionModel, setRowSelectionModel] = React.useState<GridRowSelectionModel>([]);

  const columns: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'name', headerName: 'Nome', width: 130 },
    { field: 'email', headerName: 'E-mail', width: 130 },
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
              selectedIDs.forEach((e) => {
                axios({
                  method: 'delete',
                  url: "http://localhost:8080/client/delete?id=" + e,
                })
              })

              setData((r) => r.filter((x:any) => !selectedIDs.has(x.id)));
            }}
          >
            <DeleteIcon />
          </IconButton>
        );
      }
    }
  ];


  useEffect(() => {
    axios({
      method: 'get',
      url: 'http://localhost:8080/client/getAll',
    }).then((response) => {
      if (response.data) {
        setData(response.data);
      }
    });
  }, [])



  return (
    <div style={{ height: 400, width: '100%' }}>
      <DataGrid
        rows={dataClients}
        columns={columns}
        initialState={{
          pagination: {
            paginationModel: { page: 0, pageSize: 5 },
          },
        }}
        pageSizeOptions={[5, 10]}
        checkboxSelection
        onRowSelectionModelChange={(newRowSelectionModel) => {
          setRowSelectionModel(newRowSelectionModel);
        }}
        rowSelectionModel={rowSelectionModel}
      />
    </div>
  );
}