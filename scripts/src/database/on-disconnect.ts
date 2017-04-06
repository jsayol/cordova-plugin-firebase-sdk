import { exec, ErrorCallback, SuccessCallback } from '../utils';

export class OnDisconnect {
  constructor(private _path: string) {

  }

  set(value: any): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'database_onDisconnect_set', [this._path, value]);
    });
  }

  remove(): Promise<void> {
    return this.set(null);
  }

  update(value: any): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'database_onDisconnect_update', [this._path, value]);
    });
  }

  cancel(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'database_onDisconnect_cancel', [this._path]);
    });
  }

  setWithPriority(value: any, priority: number | string | null): Promise<void> {
    return this.set({
      '.value': value,
      '.priority': priority,
    });
  }

}
