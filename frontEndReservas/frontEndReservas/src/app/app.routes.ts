// filepath: d:\Ingenieria\7 semestre\IS III\P2\frontEndReservas\frontEndReservas\src\app\app.routes.ts
import { Routes } from '@angular/router';
import { ReservasListComponent } from './reservas/reservas-list/reservas-list.component';
import { ReservaFormComponent } from './reservas/reserva-form/reserva-form.component';
import { EditarReservaComponent } from './reservas/editar-reserva/editar-reserva.component';

export const routes: Routes = [
    { path: '', component: ReservasListComponent, pathMatch:'full'}, // Home principal
    { path: 'listaReservas', component: ReservasListComponent }, // Subpágina para ver la lista de reservas
    { path: 'editar-reserva/:id', component: EditarReservaComponent }, // Subpágina para editar reserva
    { path: 'add-reserva', component: ReservaFormComponent }, // Subpágina para añadir reserv
];