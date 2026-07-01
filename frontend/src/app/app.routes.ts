import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'bicicletas', pathMatch: 'full' },

  {
    path: 'login',
    loadComponent: () =>
      import('./components/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'bicicletas',
    loadComponent: () =>
      import('./components/bicicleta-list/bicicleta-list.component').then(m => m.BicicletaListComponent)
  },
  {
    path: 'bicicletas/nuevo',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./components/bicicleta-form/bicicleta-form.component').then(m => m.BicicletaFormComponent)
  },
  {
    path: 'bicicletas/editar/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./components/bicicleta-form/bicicleta-form.component').then(m => m.BicicletaFormComponent)
  },

  { path: '**', redirectTo: 'bicicletas' }
];
