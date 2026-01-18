import { Routes } from '@angular/router';
import { BeneficiosListComponent } from './beneficios-list.component';
import { BeneficiosFormComponent } from './beneficios-form.component';

export const BENEFICIOS_ROUTES: Routes = [
    { path: '', component: BeneficiosListComponent },
    { path: 'novo', component: BeneficiosFormComponent },
    { path: 'editar/:id', component: BeneficiosFormComponent }
];