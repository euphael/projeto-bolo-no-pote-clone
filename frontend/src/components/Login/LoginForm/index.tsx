import React, { useContext, useEffect } from "react";
import { AuthContext } from "../../../context/AuthContext";
import { Link, useNavigate } from "react-router-dom";
import { Button, Typography, Box } from '@mui/material';
import Input from "../../Forms/Input";
import useForm from "../Hooks/useForm";
import axios from "axios";

const LoginForm = () => {
  const email = useForm('email');
  const password = useForm();
  const navigate = useNavigate();
  const { setAuth } = useContext(AuthContext)
  useEffect(() => {
    const token = window.localStorage.getItem('token');
    if (token) navigate('/', { replace: true });
  }, []);

  function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    
      axios({
      method: 'post',
      url: 'http://localhost:8080/auth/signin',
      withCredentials: false,
      data: {
        email: email.value,
        password: password.value,
      }
    }).then((response) => {
      if (response.status === 200) {
        setAuth(true);
        localStorage.setItem("login", JSON.stringify({ token: response.data.token, email: email.value, id: response.data.userId}));
        navigate('/', { replace: true });
      }
    })
    .catch((e)=> {
      console.log(e);
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
      <Typography variant="h4" gutterBottom>Login</Typography>
      <form onSubmit={handleSubmit} style={{width: '25vw'}}>
        <Input label="E-mail" type="email" name="email" {...email} />
        <Input label="Senha" type="password" name="password" {...password} />
        <Button sx={{ marginTop: '20px' }} variant="contained" color="primary" type="submit">
          Entrar
        </Button>
      </form>
      <Button sx={{ marginTop: '30px' }} color="inherit" component={Link} to="criar">
        Cadastrar
      </Button>
    </Box>
  );
}

export default LoginForm;
