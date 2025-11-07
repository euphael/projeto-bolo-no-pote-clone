import { Product } from "./Product";

export interface CartProduct{
    product: Product,
    quantity: Number,
    total?: Number
    
}