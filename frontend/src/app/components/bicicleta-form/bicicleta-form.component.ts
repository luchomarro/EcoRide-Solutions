import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { BicicletaService } from '../../services/bicicleta.service';
import { Bicicleta, EstadoBicicleta } from '../../models/bicicleta.model';

@Component({
  selector: 'app-bicicleta-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './bicicleta-form.component.html',
  styleUrl: './bicicleta-form.component.css'
})
export class BicicletaFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private bicicletaService = inject(BicicletaService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  /** Estados válidos para el <select>. */
  readonly estados = Object.values(EstadoBicicleta);

  id?: number;
  cargando = false;
  error = '';

  form = this.fb.nonNullable.group({
    nombre: ['', Validators.required],
    descripcion: [''],
    precioHora: [0, [Validators.required, Validators.min(0.01)]],
    estado: [EstadoBicicleta.DISPONIBLE, Validators.required],
    urlImagen: ['']
  });

  /** true cuando la ruta trae :id (modo edición). */
  get esEdicion(): boolean {
    return this.id !== undefined;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.id = Number(idParam);
      this.cargarBicicleta(this.id);
    }
  }

  private cargarBicicleta(id: number): void {
    this.cargando = true;
    this.bicicletaService.buscarPorId(id).subscribe({
      next: (b) => {
        this.form.patchValue({
          nombre: b.nombre,
          descripcion: b.descripcion ?? '',
          precioHora: b.precioHora,
          estado: b.estado,
          urlImagen: b.urlImagen ?? ''
        });
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se encontró la bicicleta solicitada.';
        this.cargando = false;
      }
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.error = '';
    this.cargando = true;
    const datos = this.form.getRawValue() as Bicicleta;

    const peticion = this.esEdicion
      ? this.bicicletaService.actualizar(this.id!, datos)
      : this.bicicletaService.crear(datos);

    peticion.subscribe({
      next: () => this.router.navigate(['/bicicletas']),
      error: (err) => {
        this.cargando = false;
        this.error =
          err.status === 401
            ? 'Tu sesión expiró. Vuelve a iniciar sesión.'
            : 'No se pudo guardar la bicicleta. Revisa los datos e inténtalo de nuevo.';
      }
    });
  }
}
