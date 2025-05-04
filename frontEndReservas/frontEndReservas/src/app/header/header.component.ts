import { Component } from '@angular/core';
@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  public titulo: string = 'Sistema de reserva de salones';
  public facultad: string = 'Facultad de Electronica y telecomunicaciones';
  public nombreUsuario: string = 'George Brown';
  public rol: string = 'Administrador';
}
