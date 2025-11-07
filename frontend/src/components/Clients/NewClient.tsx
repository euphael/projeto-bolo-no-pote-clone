import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import {useState } from "react";

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

    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const [nomeUsr, setNomeUsr] = useState('');
    const [emailUsr, setEmailUsr] = useState('');

  
    return (
      <div>
        <Button style={{marginTop: '2rem'}}variant="contained" onClick={handleOpen}>Novo Cliente</Button>
        <Modal
          open={open}
          onClose={handleClose}
          aria-labelledby="modal-modal-title"
          aria-describedby="modal-modal-description">
          <Box sx={style}>
            <Container>
              <h4>Novo Cliente</h4>
              <Grid container spacing={2} style={{paddingTop: '2rem', fontSize: '1.5rem'}}>
                <Grid item xs={12} sm={12}>
                  <TextField
                    name="nome"
                    required
                    fullWidth
                    id="nome"
                    label="Nome"
                    onChange={e => setNomeUsr(e.currentTarget.value)}
                    value={nomeUsr}
                    // {...register(label, {
                    //   required: {
                    //     value: true,
                    //     message: 'required',
                    //   },
                    // })}
                  />
                </Grid>
                <Grid item xs={12} sm={12}>
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
                {/* <Grid item xs={12}>
                  <TextField
                    fullWidth
                    name="telefone"
                    label="Telefone"
                    type="number"
                    id="telefone"
                    autoComplete="new-password"
                  />
                </Grid> */}
            </Grid>
              {/* <Button style={{marginTop: '2rem'}}variant="contained" onClick={handleClick()}>Cadastrar</Button> */}
            </Container>
          </Box>
        </Modal>
      </div>
    );




}

export default NewClient;