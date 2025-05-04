// filepath: d:\Ingenieria\7 semestre\IS III\P2\frontEndReservas\frontEndReservas\src\app\app.routes.ts
import { Routes } from '@angular/router';
import { ReservasListComponent } from './reservas-list/reservas-list.component';
import { ReservaFormComponent } from './reserva-form/reserva-form.component';

export const routes: Routes = [
    { path: '', component: ReservasListComponent }, // Home principal
    { path: 'add-reserva', component: ReservaFormComponent } // Subpágina para añadir reserva
];