import { Routes } from '@angular/router';
import { BeneficiosListComponent } from './beneficios-list.component';
import { BeneficiosFormComponent } from './beneficios-form.component';
import { BeneficiosTransferComponent } from './beneficios-transfer.component';

export const BENEFICIOS_ROUTES: Routes = [
    { path: '', component: BeneficiosListComponent },
    { path: 'novo', component: BeneficiosFormComponent },
    { path: 'editar/:id', component: BeneficiosFormComponent },
    { path: 'transferir/:id', component: BeneficiosTransferComponent }
];