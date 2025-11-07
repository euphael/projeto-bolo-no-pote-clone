import { Image } from "react-bootstrap"
import './css/WhatsappBtn.css';

export default function WhatsappBtn(){

    return (
        <a href="https://api.whatsapp.com/send?phone=5531987136990&text=Ol%C3%A1,%20gostaria%20de%20realizar%20um%20pedido!" target="blank">
            <Image src="whatsappBtn.png" className="whatsappBtn"></Image>
        </a>
    )


}