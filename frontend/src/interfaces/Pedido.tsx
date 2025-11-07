import { Product } from "./Product";
import { ProductPedido } from "./ProductPedido";

export interface Pedido {
    id?: number,
    product: ProductPedido,
    userId: Number,
    endDate: String
    comment?: String,
    feedbacks?: [],
    status?: String,
    user?: {},
    productsStore?: Product;
    quantity?: Number,
}