import { useState } from "react";
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import FormControl from '@mui/material/FormControl';
import {Input, InputLabel } from '@mui/material';
import Container from '@mui/material/Container';

const style = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 500,
    bgcolor: 'background.paper',
    boxShadow: 24,
    p: 4,
  };

const NewProduct = () => {

    const [open, setOpen] = useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    let name = '';
    let price = '';
    let keyWords = '';
    let weight = '';

    const addNewProduct = () => {
    }

    return (
      <div>
        <Button style={{marginTop: '2rem'}}variant="contained" onClick={handleOpen}>Novo Produto</Button>
        <Modal
          open={open}
          onClose={handleClose}
          aria-labelledby="modal-modal-title"
          aria-describedby="modal-modal-description">
          <Box sx={style}>
            <Container>
              <h4>Novo Produto</h4>

              <FormControl fullWidth={true} margin="dense" style={{marginRight: '2rem'}}>
                <InputLabel  htmlFor="name">Nome</InputLabel>
                <Input id="name" value={name} />
              </FormControl>

              <FormControl fullWidth={true} margin="dense">
                <InputLabel htmlFor="keywords">Palavras Chaves</InputLabel>
                <Input id="keywords" value={keyWords}/> 
              </FormControl>

              <FormControl style={{marginRight: '2rem'}}margin="dense">
                <InputLabel htmlFor="price">Preço</InputLabel>
                <Input id="price" value={price} />
              </FormControl>

              <FormControl margin="dense">
                <InputLabel htmlFor="weight">Peso</InputLabel>
                <Input id="weight" value={weight}/>
              </FormControl>

              
              <FormControl fullWidth={true} margin="dense">
                <InputLabel htmlFor="image">Imagem</InputLabel>
                <Input  type="file" id="image" value={weight}/>
              </FormControl>

              <Button fullWidth={true} onClick={addNewProduct} style={{marginTop: '2rem'}}variant="contained">Cadastrar</Button>

            </Container>
          </Box>
        </Modal>
      </div>
    );




}

export default NewProduct;