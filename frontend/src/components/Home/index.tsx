import { Typography, Button } from '@mui/material';

const Home = () => {
  return <div>
    <div style={{backgroundColor: "#000000", height: '70vh'}}>
      <img src='jars-with-berries.jpg' style={{width: '100%', height: '70vh', objectFit: 'cover', objectPosition: '40% 30%', opacity: '50%'}}></img>
      <div style={{position: 'relative', marginTop: '-20rem', marginLeft: '8rem'}}>
        <Typography style={{fontSize: '3rem', fontWeight: '700', color: "#FFFFFF"}}>Novos Sabores</Typography>
        <Button variant="contained" style={{fontSize: '1.5rem', backgroundColor:"#FFA800", color: '#000000', fontWeight: '800'}} href='/catalogo'>Conheça já</Button>
      </div>

    </div>
  </div>
} 

export default Home;