import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import { useState } from "react";
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import Swal from 'sweetalert2';
import axios from 'axios';

const style = {
  position: 'absolute' as 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  boxShadow: 24,
  p: 4,
};

const NewClient = () => {
  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const [nomeUsr, setNomeUsr] = useState('');
  const [emailUsr, setEmailUsr] = useState('');
  const [passwordUsr, setPasswordUsr] = useState('');
  const [roleUsr, setRoleUsr] = useState('');

  const Toast = Swal.mixin({
    toast: true,
    position: "top-end",
    showConfirmButton: false,
    timer: 5000,
    timerProgressBar: true,
    didOpen: (toast) => {
      toast.onmouseenter = Swal.stopTimer;
      toast.onmouseleave = Swal.resumeTimer;
    }
  });

  const handleClick = async () => {
    console.log('Nome:', nomeUsr);
    console.log('E-mail:', emailUsr);
    console.log("Password: ", passwordUsr);
    console.log("Role: ", roleUsr);

    try {
      if (!roleUsr) {
        Toast.fire({
          icon: "error",
          title: "Selecione uma permissão."
        });
        return;
      }
      const response = await axios.post('http://localhost:8080/client/create', {
        name: nomeUsr,
        email: emailUsr,
        password: passwordUsr,
        role: roleUsr
      });
      if (response.status === 201) {
        setNomeUsr('');
        setEmailUsr('');
        setPasswordUsr('');
        setRoleUsr('');
        handleClose();
        Swal.fire({
          icon: 'success',
          title: 'Cliente criado com sucesso!',
          showConfirmButton: false,
          timer: 1500
        });
      } else {
        Toast.fire({
          icon: "error",
          title: "Erro ao criar um cliente."
        });
      }
    } catch (e) {
      Toast.fire({
        icon: "error",
        title: "Erro ao criar um cliente."
      });
    }
  }

  return (
    <div>
      <Button style={{ marginTop: '2rem' }} variant="contained" onClick={handleOpen}>Novo Cliente</Button>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description">
        <Box sx={style}>
          <Container>
            <h4>Novo Cliente</h4>
            <Grid container spacing={2} style={{ paddingTop: '2rem', fontSize: '1.5rem' }}>
              <Grid item xs={12} sm={12}>
                <TextField
                  name="nome"
                  required
                  fullWidth
                  id="nome"
                  label="Nome"
                  onChange={e => setNomeUsr(e.target.value)}
                  value={nomeUsr}
                />
              </Grid>
              <Grid item xs={12} sm={12}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  label="E-mail"
                  name="email"
                  value={emailUsr}
                  onChange={e => setEmailUsr(e.target.value)}
                />
              </Grid>
              <Grid item xs={12} sm={12}>
                <TextField
                  required
                  fullWidth
                  id="password"
                  label="Senha"
                  name="password"
                  type="password"
                  value={passwordUsr}
                  onChange={e => setPasswordUsr(e.target.value)}
                />
              </Grid>
              <Grid item xs={12} sm={12}>
                <Select
                  fullWidth
                  id="role"
                  label="Papel"
                  value={roleUsr}
                  onChange={e => setRoleUsr(e.target.value)}
                >
                  <MenuItem value="ROLE_CUSTOMER">Cliente</MenuItem>
                  <MenuItem value="ROLE_ADMINISTRATOR">Administrador</MenuItem>
                </Select>
              </Grid>
            </Grid>
            <Button fullWidth={true} onClick={handleClick} style={{ marginTop: '2rem' }} variant="contained">Cadastrar</Button>
          </Container>
        </Box>
      </Modal>
    </div>
  );
}

export default NewClient;
