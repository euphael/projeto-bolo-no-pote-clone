import { useEffect, useState } from "react";
import Container from '@mui/material/Container';
import Divider from '@mui/material/Divider';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import axios from "axios";
import Snackbar from '@mui/material/Snackbar';

const EditarPerfil = () => {
  const [idUsr, setIdUsr] = useState('');
  const [nomeUsr, setNomeUsr] = useState('');
  const [messageBox, setMessageBox] = useState('');
  const [emailUsr, setEmailUsr] = useState('');
  const [open, setOpen] = useState(false);

  useEffect(() => {
    const userData = localStorage.getItem("login");
    if(typeof userData == 'string'){
      const data = JSON.parse(userData);
      axios.get("http://localhost:8080/client/getAll").then((response) => {
        Object.values(response.data).forEach((e:any) => {
          if (e.email == data.email) {
            setIdUsr(e.id);
            setNomeUsr(e.name);
            setEmailUsr(e.email);
          }
        })
      })
    }
  }, []);

  const handleClose = (reason?: string) => {
    if (reason === 'clickaway') {
      return;
    }

    setOpen(false);
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const password = data.get("senha");
    const confirmPassword = data.get("confirmaSenha");
    // if (data.get("senha").length < 6) {
    //   setMessageBox("Sua senha precisa ter pelo menos 6 caracteres!");
    //   setOpen(true);
    //   return false
    // }
    if (password !== confirmPassword) {
      setMessageBox("As senhas não coicidem!");
      setOpen(true);
      return false;
    }

    axios({
      method: 'put',
      url: "http://localhost:8080/client/update?id=" + idUsr,
      data: {
        name: data.get("nome"),
        email: data.get("email"),
        password: data.get("senha"),
      }
    }).then((response) => {
      if (response.status === 201) {
        setMessageBox("Dados atualizados com sucesso!");
        setOpen(true);
      }
    });
  }


  return (
    <Container maxWidth="lg">
      <Snackbar
        open={open}
        autoHideDuration={3000}
        message={messageBox}
        onClose={()=> handleClose()}
      />
      <h3 style={{ marginTop: '2rem' }}>Meu Perfil</h3>
      <Divider style={{ marginBottom: '2rem' }} />
      <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
        <h4 style={{ marginBottom: '2rem' }}>Dados Pessoais</h4>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={12}>
            <TextField
              name="nome"
              required
              fullWidth
              id="nome"
              label="Nome"
              onChange={e => setNomeUsr(e.currentTarget.value)}
              value={nomeUsr}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              required
              fullWidth
              id="email"
              label="E-mail"
              name="email"
              defaultValue=""
              value={emailUsr}
              onChange={e => setEmailUsr(e.currentTarget.value)}
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              fullWidth
              name="telefone"
              label="Telefone"
              type="number"
              id="telefone"
              autoComplete="new-password"
            />
          </Grid>
        </Grid>
        <h4 style={{ marginBottom: '2rem', marginTop: '2rem' }}>Atualizar Senha</h4>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              id="senha"
              label="Senha"
              type="password"
              name="senha"
              autoComplete="senha"
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              fullWidth
              name="confirmaSenha"
              label="Confirmar Senha"
              type="password"
              id="confirmaSenha"
              autoComplete="new-password"
            />
          </Grid>
        </Grid>
        <Button
          type="submit"
          fullWidth
          variant="contained"
          sx={{ mt: 3, mb: 2 }}
        >
          Atualizar
        </Button>
      </Box>
    </Container>


  )


}

export default EditarPerfil