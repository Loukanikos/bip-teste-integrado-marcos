import { Routes } from '@angular/router';
import { BENEFICIOS_ROUTES } from './beneficios/beneficios.routes';

export const routes: Routes = [
    {
        path: 'beneficios',
        children: BENEFICIOS_ROUTES
    },
    { path: '', redirectTo: 'beneficios', pathMatch: 'full' }
];
