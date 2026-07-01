export enum EstadoBicicleta {
  DISPONIBLE = 'DISPONIBLE',
  EN_USO = 'EN_USO',
  MANTENIMIENTO = 'MANTENIMIENTO'
}

export interface Bicicleta {
  id?: number;
  nombre: string;
  descripcion?: string;
  precioHora: number;
  estado: EstadoBicicleta;
  urlImagen?: string;
}

/**
 * Refleja la estructura de un `Page<Bicicleta>` de Spring Data
 * devuelta por GET /api/bicicletas/paginado
 */
export interface PageBicicleta {
  content: Bicicleta[];
  totalElements: number;
  totalPages: number;
  number: number;          // índice de la página actual (empieza en 0)
  size: number;
  numberOfElements: number;
  first: boolean;
  last: boolean;
}
