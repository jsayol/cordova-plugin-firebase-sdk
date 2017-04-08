import { ErrorCallback, SuccessCallback } from '../utils';
import { Database } from './index';

export class OnDisconnect {
  constructor(private _db: Database, private _path: string) {

  }

  private _exec(success: SuccessCallback, error: ErrorCallback, action: string, args?: any[]) {
    this._db._exec(success, error, `onDisconnect_${action}`, args);
  }

  set(value: any): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'set', [this._path, value]);
    });
  }

  remove(): Promise<void> {
    return this.set(null);
  }

  update(value: any): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'update', [this._path, value]);
    });
  }

  cancel(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'cancel', [this._path]);
    });
  }

  setWithPriority(value: any, priority: number | string | null): Promise<void> {
    return this.set({
      '.value': value,
      '.priority': priority,
    });
  }

}
