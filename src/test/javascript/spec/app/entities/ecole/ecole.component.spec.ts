/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { AppEcoleTestModule } from '../../../test.module';
import { EcoleComponent } from 'app/entities/ecole/ecole.component';
import { EcoleService } from 'app/entities/ecole/ecole.service';
import { Ecole } from 'app/shared/model/ecole.model';

describe('Component Tests', () => {
    describe('Ecole Management Component', () => {
        let comp: EcoleComponent;
        let fixture: ComponentFixture<EcoleComponent>;
        let service: EcoleService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AppEcoleTestModule],
                declarations: [EcoleComponent],
                providers: []
            })
                .overrideTemplate(EcoleComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(EcoleComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EcoleService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Ecole(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.ecoles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
