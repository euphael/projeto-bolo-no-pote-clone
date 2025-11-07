import DataTableOrders from './DataTableOrders';
import Container from '@mui/material/Container';


const Pedidos = () => {

    return (
        <Container style={{ marginTop: '2rem' }}>
            <h4 style={{ marginBottom: '2rem', fontSize: '3rem' }}>Pedidos Realizados</h4>
            <DataTableOrders />
        </Container>
    )



}

export default Pedidos