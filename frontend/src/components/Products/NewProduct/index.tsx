import { ChangeEvent, useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import FormControl from '@mui/material/FormControl';
import { Input, InputLabel } from '@mui/material';
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
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [weight, setWeight] = useState<number>(0);
  const [keyWords, setKeyWords] = useState('');
  const [image, setImage] = useState<File | undefined>(undefined);

  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleNameChange = (e: ChangeEvent<HTMLInputElement>) => setName(e.target.value);
  const handlePriceChange = (e: ChangeEvent<HTMLInputElement>) => setPrice(e.target.value);
  const handleKeyWordsChange = (e: ChangeEvent<HTMLInputElement>) => setKeyWords(e.target.value);
  const handleWeightChange = (e: ChangeEvent<HTMLInputElement>) => setWeight(Number(e.target.value));
  const handleImageChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setImage(e.target.files[0]);
    }
  };

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

  const addNewProduct = async () => {
    if (!image) {
      Toast.fire({
        icon: "error",
        title: "Selecione um arquivo de imagem."
      });
      return;
    }

    const formData = new FormData();
    formData.append('name', name);
    formData.append('price', price);
    formData.append('keyWords', keyWords);
    formData.append('weight', weight.toString());
    formData.append('file', image);

    try {
      await axios.post('http://localhost:8080/product/create', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      setName('');
      setPrice('');
      setWeight(0);
      setKeyWords('');
      setImage(undefined);
      handleClose();
    } catch (error) {
      Toast.fire({
        icon: "error",
        title: "Erro ao criar um novo produto."
      });
      return;
    }
  };

  return (
    <div>
      <Button style={{ marginTop: '2rem' }} variant="contained" onClick={handleOpen}>Novo Produto</Button>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description">
        <Box sx={style}>
          <Container>
            <h4>Novo Produto</h4>

            <FormControl fullWidth={true} margin="dense" style={{ marginRight: '2rem' }}>
              <InputLabel htmlFor="name">Nome</InputLabel>
              <Input id="name" value={name} onChange={handleNameChange} />
            </FormControl>

            <FormControl fullWidth={true} margin="dense">
              <InputLabel htmlFor="keywords">Palavras Chaves</InputLabel>
              <Input id="keywords" value={keyWords} onChange={handleKeyWordsChange} />
            </FormControl>

            <FormControl style={{ marginRight: '2rem' }} margin="dense">
              <InputLabel htmlFor="price">Preço</InputLabel>
              <Input id="price" value={price} onChange={handlePriceChange} />
            </FormControl>

            <FormControl margin="dense">
              <InputLabel htmlFor="weight">Peso</InputLabel>
              <Input id="weight" value={weight} onChange={handleWeightChange} />
            </FormControl>

            <FormControl fullWidth={true} margin="dense">
              <InputLabel htmlFor="image">Imagem</InputLabel>
              <Input type="file" id="image" onChange={handleImageChange} />
            </FormControl>

            <Button fullWidth={true} onClick={addNewProduct} style={{ marginTop: '2rem' }} variant="contained">Cadastrar</Button>
          </Container>
        </Box>
      </Modal>
    </div>
  );
}

export default NewProduct;