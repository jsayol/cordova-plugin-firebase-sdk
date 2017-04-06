import { exec, ErrorCallback, SuccessCallback } from '../utils';
import { Reference } from './reference';
import { DataSnapshot } from './data-snapshot';
import { Path } from './utils/path';

export class Database {
  private static _instance: Database;

  /**
   * @internal
   */
  _listeners: { [k: string]: Listener } = {};

  /**
   * @internal
   */
  _listenerCounter = 1;


  constructor() {
    // Prevent creating multiple instances
    if (Database._instance) {
      return Database._instance;
    }
  }

  static getInstance(): Database {
    if (!this._instance) {
      this._instance = new Database();
    }

    return this._instance;
  }

  ref(path: string): Reference {
    return new Reference(path, this);
  }

  goOffline(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'database_goOffline', []);
    });
  }

  goOnline(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'database_goOnline', []);
    });
  }

  setPersistenceEnabled(isEnabled: boolean): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'database_setPersistenceEnabled', [isEnabled]);
    });
  }

}

export type EventListenerCallback = (snapshot: DataSnapshot, prevChildKey?: string) => any;

/**
 * @internal
 */
export interface Listener {
  path: Path;
  eventType: string;
  callback: Function;
}

export interface IDatabase {
  (): Database;
  ServerValue: {
    TIMESTAMP: object;
  };
}

export const database = <IDatabase>(() => Database.getInstance());

database.ServerValue = {
  TIMESTAMP: {
    '.sv': 'timestamp'
  }
};

export { Reference, DataSnapshot };
export { Query } from './query';
export { ThenableReference } from './reference';
export { OnDisconnect } from './on-disconnect';
