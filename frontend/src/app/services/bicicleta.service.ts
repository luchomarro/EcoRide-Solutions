import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Bicicleta, PageBicicleta } from '../models/bicicleta.model';

@Injectable({ providedIn: 'root' })
export class BicicletaService {
  private readonly baseUrl = `${environment.apiUrl}/api/bicicletas`;

  constructor(private http: HttpClient) {}

  /** GET /api/bicicletas — inventario completo sin paginar. */
  listar(): Observable<Bicicleta[]> {
    return this.http.get<Bicicleta[]>(this.baseUrl);
  }

  /** GET /api/bicicletas/paginado?page&size&sort */
  listarPaginado(page = 0, size = 5, sort = 'id'): Observable<PageBicicleta> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);
    return this.http.get<PageBicicleta>(`${this.baseUrl}/paginado`, { params });
  }

  /** GET /api/bicicletas/{id} */
  buscarPorId(id: number): Observable<Bicicleta> {
    return this.http.get<Bicicleta>(`${this.baseUrl}/${id}`);
  }

  /** POST /api/bicicletas (requiere JWT) */
  crear(bicicleta: Bicicleta): Observable<Bicicleta> {
    return this.http.post<Bicicleta>(this.baseUrl, bicicleta);
  }

  /** PUT /api/bicicletas/{id} (requiere JWT) */
  actualizar(id: number, bicicleta: Bicicleta): Observable<Bicicleta> {
    return this.http.put<Bicicleta>(`${this.baseUrl}/${id}`, bicicleta);
  }

  /** DELETE /api/bicicletas/{id} (requiere JWT) */
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
