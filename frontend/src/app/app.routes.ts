import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { BicicletaListComponent } from './components/bicicleta-list/bicicleta-list.component';
import { BicicletaFormComponent } from './components/bicicleta-form/bicicleta-form.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  // Página de inicio con hero, beneficios y catálogo
  { path: '', component: HomeComponent },

  // Login standalone (ruta directa, no solo modal)
  { path: 'login', component: LoginComponent },

  // Inventario completo
  { path: 'bicicletas', component: BicicletaListComponent },

  // Crear y editar: protegidas con authGuard
  { path: 'bicicletas/nuevo',       component: BicicletaFormComponent, canActivate: [authGuard] },
  { path: 'bicicletas/editar/:id',  component: BicicletaFormComponent, canActivate: [authGuard] },

  { path: '**', redirectTo: '' }
];
