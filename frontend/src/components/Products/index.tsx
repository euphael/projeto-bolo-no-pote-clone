import DataTableProducts from './DataTableProducts.tsx';
import Container from '@mui/material/Container';
import NewProduct from './NewProduct/index.tsx';

const Produtos = () => {
    return (
        <Container maxWidth="md" style={{ marginTop: '2rem' }}>
            <h4 style={{ marginBottom: '2rem' }}>Produtos Cadastrados</h4>
            <DataTableProducts />
            <NewProduct />
        </Container>
    )
}

export default Produtos;