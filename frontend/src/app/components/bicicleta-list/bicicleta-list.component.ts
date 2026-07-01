import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BicicletaService } from '../../services/bicicleta.service';
import { AuthService } from '../../services/auth.service';
import { Bicicleta, PageBicicleta } from '../../models/bicicleta.model';

@Component({
  selector: 'app-bicicleta-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './bicicleta-list.component.html',
  styleUrl: './bicicleta-list.component.css'
})
export class BicicletaListComponent implements OnInit {
  pagina?: PageBicicleta;
  cargando = false;
  error = '';

  page = 0;
  size = 5;
  readonly sort = 'id';

  constructor(
    private bicicletaService: BicicletaService,
    public auth: AuthService
  ) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    this.bicicletaService.listarPaginado(this.page, this.size, this.sort).subscribe({
      next: (p) => {
        this.pagina = p;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo cargar el inventario. ¿Está corriendo la API en http://localhost:8081?';
        this.cargando = false;
      }
    });
  }

  irA(page: number): void {
    if (!this.pagina) return;
    if (page < 0 || page > this.pagina.totalPages - 1) return;
    this.page = page;
    this.cargar();
  }

  eliminar(b: Bicicleta): void {
    if (!b.id) return;
    if (!confirm(`¿Eliminar la bicicleta "${b.nombre}"?`)) return;

    this.bicicletaService.eliminar(b.id).subscribe({
      next: () => {
        // Si al borrar quedó la página vacía y no es la primera, retrocede una.
        if (this.pagina && this.pagina.content.length === 1 && this.page > 0) {
          this.page--;
        }
        this.cargar();
      },
      error: (err) => {
        this.error =
          err.status === 401
            ? 'Tu sesión expiró. Vuelve a iniciar sesión.'
            : 'No se pudo eliminar la bicicleta.';
      }
    });
  }

  badgeClase(estado: string): string {
    switch (estado) {
      case 'DISPONIBLE': return 'bg-success';
      case 'EN_USO': return 'bg-primary';
      case 'MANTENIMIENTO': return 'bg-warning text-dark';
      default: return 'bg-secondary';
    }
  }
}
