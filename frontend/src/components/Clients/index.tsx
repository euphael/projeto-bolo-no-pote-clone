import DataTable from "./DataTable.tsx";
import Container from '@mui/material/Container';
import NewClient from "./NewClient/index.tsx";

const Clientes = () => {

    return(
        <Container maxWidth="md" style={{marginTop: '2rem'}}>
            <h4 style={{marginBottom: '2rem'}}>Clientes Cadastrados</h4>
            <DataTable/>
            <NewClient/>
        </Container>
    )

}

export default Clientes;