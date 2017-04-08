import { ErrorCallback, SuccessCallback } from '../utils';
import { Reference } from './reference';
import { DataSnapshot } from './data-snapshot';
import { Path } from './utils/path';
import { App } from '../app';

export class Database {
  /**
   * @internal
   */
  _listeners: { [k: string]: Listener } = {};

  /**
   * @internal
   */
  _listenerCounter = 1;

  /**
   * @internal
   */
  constructor(public app: App) {

  }

  /**
   * @internal
   */
  _exec(success: SuccessCallback, error: ErrorCallback, action: string, args?: any[]) {
    this.app._exec(success, error, `database_${action}`, args);
  }

  ref(path: string): Reference {
    return new Reference(path, this);
  }

  goOffline(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'goOffline');
    });
  }

  goOnline(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'goOnline');
    });
  }

  setPersistenceEnabled(isEnabled: boolean): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'setPersistenceEnabled', [isEnabled]);
    });
  }

}

/**
 * @internal
 */
export interface Listener {
  path: Path;
  eventType: string;
  callback: Function;
}

export type EventListenerCallback = (snapshot: DataSnapshot, prevChildKey?: string) => any;

export const ServerValue = {
  TIMESTAMP: {
    '.sv': 'timestamp',
  },
};

export { Reference, DataSnapshot };
export { Query } from './query';
export { ThenableReference } from './reference';
export { OnDisconnect } from './on-disconnect';
