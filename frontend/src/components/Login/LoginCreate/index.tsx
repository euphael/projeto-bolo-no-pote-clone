import React, { useEffect, useState } from 'react';
import { Button, Box, Typography } from '@mui/material';
import Input from "../../Forms/Input";
import useForm from "../Hooks/useForm";
import { useNavigate } from 'react-router-dom';
import axios from 'axios';


const LoginCreate: React.FC = () => {
  const email = useForm('email');
  const password = useForm('password');
  const confirmPassword = useForm('password');
  const navigate = useNavigate();
  const [passwordsMatch, setPasswordsMatch] = useState(true);

  useEffect(() => {
    const token = window.localStorage.getItem('token');
    if (token) navigate('/', { replace: true });
  }, []);


  function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (password.value !== confirmPassword.value) {
      setPasswordsMatch(false);
      return;
    }

    axios({
      method: 'post',
      url: 'http://localhost:8080/auth/signup',
      withCredentials: false,
      data: {
        email: email.value,
        password: password.value,
        role: "ROLE_CUSTOMER"
      }
    }).then((response) => {
      if (response.status === 201) {
        navigate('/login', { replace: true });
      }
    });
  }
  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      minHeight="100vh"
    >
      <Typography variant="h4" component="h1" gutterBottom>
        Registrar
      </Typography>
      <form onSubmit={handleSubmit} style={{width: '25vw'}}>
        <Input label="E-mail" type="email" name="email" {...email} />
        <Input label="Senha" type="password" name="password" {...password} />
        <Input label="Confirmar Senha" type="password" name="confirmPassword" {...confirmPassword} />
        {!passwordsMatch && (
          <Typography variant="body2" color="error">
            As senhas não coincidem.
          </Typography>
        )}
        <Button sx={{ marginTop: '20px' }} variant="contained" color="primary" type="submit" >
          Cadastrar
        </Button>
      </form>
    </Box>
  );
}

export default LoginCreate;
