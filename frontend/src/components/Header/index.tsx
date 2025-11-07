import { AppBar, Toolbar, Button, Box } from '@mui/material';
import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';

function Header() {
  const { auth } = useContext(AuthContext);
  return (
    <AppBar position="static">
      <Toolbar>
        <Box sx={{ flexGrow: 1 }}>
          <Button color="inherit" component={Link} to="/">
            Bolo no Pote
          </Button>
        </Box>
        <Box>
          {auth ? (
            <div>
              <Button color="inherit" component={Link} to="/meuspedidos">Meus Pedidos</Button>
              <Button color="inherit" component={Link} to="/meuperfil">Meu Perfil</Button>
            </div>
          ) : (
            <Button color="inherit" component={Link} to="/login">Login</Button>
          )}
        </Box>

      </Toolbar>
    </AppBar>
  );
}

export default Header;
