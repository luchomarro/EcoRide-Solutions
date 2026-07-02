import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { BicicletaService } from '../../services/bicicleta.service';
import { Bicicleta } from '../../models/bicicleta.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  // ── Catálogo desde la API ─────────────────────────────────────────────────
  bicicletas = signal<Bicicleta[]>([]);
  cargandoCatalogo = signal(true);
  navOpen = false;

  // ── Login ─────────────────────────────────────────────────────────────────
  username = '';
  password = '';
  loginError = signal<string | null>(null);
  loginCargando = signal(false);
  modalVisible = signal(false);


  constructor(
    public authService: AuthService,
    private bicicletaService: BicicletaService,
    public router: Router
  ) {}

  ngOnInit(): void {
    // Si ya está logueado, redirige directo al inventario
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/bicicletas']);
      return;
    }
    this.cargarCatalogo();
  }

  cargarCatalogo(): void {
    this.bicicletaService.listar().subscribe({
      next: (data) => {
        // Muestra máximo 3 en el home (las primeras disponibles o todas)
        this.bicicletas.set(data.slice(0, 3));
        this.cargandoCatalogo.set(false);
      },
      error: () => this.cargandoCatalogo.set(false)
    });
  }

  getImagen(bicicleta: Bicicleta): string {
    if (bicicleta.urlImagen && bicicleta.urlImagen.startsWith('http')) {
      return bicicleta.urlImagen;
    }
    const imagenesEstables: Record<number, string> = {
      1: 'https://images.pexels.com/photos/276517/pexels-photo-276517.jpeg?auto=compress&cs=tinysrgb&w=500',
      2: 'https://images.pexels.com/photos/1149601/pexels-photo-1149601.jpeg?auto=compress&cs=tinysrgb&w=500',
      3: 'https://images.pexels.com/photos/100582/pexels-photo-100582.jpeg?auto=compress&cs=tinysrgb&w=500',
      4: 'https://images.pexels.com/photos/5462549/pexels-photo-5462549.jpeg?auto=compress&cs=tinysrgb&w=500',
      5: 'https://images.pexels.com/photos/571939/pexels-photo-571939.jpeg?auto=compress&cs=tinysrgb&w=500',
    };
    return imagenesEstables[bicicleta.id ?? 1]
      ?? imagenesEstables[((bicicleta.id ?? 0) % 5) + 1];
  }

  abrirModal(): void {
    this.loginError.set(null);
    this.username = '';
    this.password = '';
    this.modalVisible.set(true);
  }

  cerrarModal(): void {
    this.modalVisible.set(false);
  }

  onLogin(): void {
    this.loginError.set(null);
    this.loginCargando.set(true);

    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.loginCargando.set(false);
        this.cerrarModal();
        this.router.navigate(['/bicicletas']);
      },
      error: () => {
        this.loginCargando.set(false);
        this.loginError.set('Usuario o contraseña incorrectos.');
      }
    });
  }

  scrollTo(id: string): void {
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' });
  }

  claseBadge(estado: string): string {
    return estado === 'DISPONIBLE' ? 'badge bg-success-subtle text-success'
         : estado === 'EN_USO'    ? 'badge bg-danger-subtle text-danger'
         : 'badge bg-secondary-subtle text-secondary';
  }

  textoEstado(estado: string): string {
    return estado === 'DISPONIBLE' ? 'Disponible'
         : estado === 'EN_USO'    ? 'En Uso'
         : 'Mantenimiento';
  }
}
