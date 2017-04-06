import { Database } from './index';
import { Query } from './query';
import { Path } from './utils/path';
import { OnDisconnect } from './on-disconnect';
import { exec, ErrorCallback, SuccessCallback } from '../utils';
import { generatePushKey } from './utils/push-key';

export class Reference extends Query {

  constructor(path: string | Path, db: Database) {
    super(path, db);
  }

  get key(): string | null {
    return this._path.key;
  }

  get parent(): Reference | null {
    const parent = this._path.parent;
    return parent ? new Reference(parent, this._db) : null;
  }

  get root(): Reference {
    return new Reference('/', this._db);
  }

  child(path: string): Reference {
    return new Reference(this._path.child(path), this._db);
  }

  set(value: any): Promise<void> {
    if (typeof value === 'undefined') {
      throw new Error('Query.set: no value specified');
    }

    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'database_set', [this._path.toString(), value]);
    });
  }

  remove(): Promise<void> {
    return this.set(null);
  }

  push(): Reference;
  push(value: any): ThenableReference;
  push(value?: any): Reference | ThenableReference {
    const pushKey = generatePushKey();

    if (typeof value === 'undefined') {
      return this.child(pushKey);
    }

    const thenableRef = new ThenableReference(this._path.child(pushKey), this._db);
    thenableRef.set(value);

    return thenableRef;
  }

  setPriority(priority: string | number | null): Promise<void> {
    const newRef = new Reference(this._path.child('.priority', true), this._db);
    return newRef.set(priority);
  }

  setWithPriority(value: any, priority: string | number | null): Promise<void> {
    return this.set({
      '.value': value,
      '.priority': priority,
    });
  }

  transaction() {
    // TODO: implement this... but how? It might not be possible.
    throw new Error('Transactions are not supported yet. Sorry!');
  }

  update(value: any) {
    if (typeof value === 'undefined') {
      throw new Error('Query.update: no value specified');
    }

    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'database_update', [this._path.toString(), value]);
    });
  }

  onDisconnect(): OnDisconnect {
    return new OnDisconnect(this._path.toString());
  }

}

export class ThenableReference extends Reference {
  private _promise: Promise<void>;

  set(value: any): Promise<void> {
    this._promise = super.set(value);
    return this._promise;
  }

  then(onfulfilled?: (() => any) | undefined | null,
       onrejected?: ((reason: any) => any) | undefined | null): Promise<void> {
    return this._promise.then<void>(onfulfilled, onrejected);
  }

  catch<T>(onrejected?: ((reason: any) => T) | undefined | null): Promise<T> {
    return this._promise.catch<T>(onrejected);
  }
}
