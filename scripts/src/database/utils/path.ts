export class Path {
  private _parts: string[];

  constructor(path: string | string[] = '') {
    this._parts = typeof path === 'string' ? Path.getParts(path) : path;
  }

  get key(): string | null {
    return this._parts.length ? this._parts[this._parts.length - 1] : null;
  }

  get parent() {
    if (!this._parts.length)
      return null;

    return new Path(this._parts.slice(0, this._parts.length - 1));
  }

  get parts() {
    return this._parts.slice(0);
  }

  child(path: string, skipCheck = false): Path {
    return new Path([...this._parts, ...Path.getParts(path, skipCheck)]);
  }

  isEqual(otherPath: Path): boolean {
    return otherPath && (this.toString() === otherPath.toString());
  }

  toString(): string {
    return '/' + this._parts.join('/');
  }

  get length(): number {
    return this._parts.length;
  }

  static getParts(path: string, skipCheck = false): string[] {
    path = Path.normalize(path, skipCheck);

    if (path === '')
      return [];

    return path.split('/');
  }

  static normalize(path: string, skipCheck = false): string {
    if (!skipCheck && /[\.\#\$\[\]]/.test(path)) {
      throw new Error(`Invalid path: "${path}". Paths must be non-empty strings and can't contain ".", "#", "$", "[", or "]"`);
    }

    return path.replace(/\/\/+/, '/').replace(/^\//, '').replace(/\/$/, '');
  }
}
