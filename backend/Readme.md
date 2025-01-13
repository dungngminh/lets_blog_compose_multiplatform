# Let's Blog Backend

[![Powered by Dart Frog](https://img.shields.io/endpoint?url=https://tinyurl.com/dartfrog-badge)](https://dartfrog.vgv.dev)

Let's Blog Backend built with `dart_frog`

## Getting Started 🤖

This is the backend side of the Let's Blog project. It's written in Dart, using Dart Frog framework, I decided to use Dart Frog because I want to try the power of Dart on backend side.

## How to run this project ❓

### Install Dart 🎯

You can find the installation guide [here](https://dart.dev/get-dart).

Or you can use brew to install Dart SDK:

```shell
brew tap dart-lang/dart
brew install dart
```

And make sure you have Dart SDK in your PATH.

### Install Dart Frog CLI 🐸

Install Dart Frog CLI by running:

```shell
pub global activate dart_frog_cli
```

### Install dependencies 📦

First, you need to install dependencies by running:

```shell
dart pub get
```

### Cloudinary configuration 🌈

- You need to create an account on [Cloudinary](https://cloudinary.com/).

- Find your cloud name, API key, and API secret in the dashboard.

- You can check the tutorial [here](https://cloudinary.com/documentation/finding_your_credentials_tutorial).

=> You got cloud name, API key and API secret.

### Postgres configuration 🐘

- You can use Postgres database via Docker or any database hosting services in any cloud providers.

- Get the info of your Postgres database, including host, port, username, password, and database name.

### Prepare env configuration 🛠

- _Currently, I filled env for KotlinConf contest, you can use it for testing in `.env.contest`._
- If you want to run this project, with your own configuration, you need to create env properties file, I also put a sample file is `.env.sample`, you can copy it to `.env` for production, `.env.local` for dev and fill your own configuration.

### Run the project 🚀

You can run the project by running via run scripts that binds commands to run Dart Frog:

```shell
./run.sh
```

Question will be asked for you to choose the environment file, `dev/prod/contest:`. Choose the one you want to run. By default, it will run with `env.contest` environment:

## Contributors 🌟

<table>
  <tr>
    <td align="center"><img src="https://avatars.githubusercontent.com/u/63831488?v=4" width="100px;" alt=""/><br /><sub><b>Dzung Nguyen Minh</b></sub></a><br /><a href="https://github.com/dungngminh/app_creaty/commits?author=dungngminh" title="Maintainer">💻</a> <a title="Document">📖</a> <a title="Fix Bug">🐛</a>  
</tr>
</table>

## More about Dart Frog 🐸

- [Dart Frog](https://dartfrog.vgv.dev)
- [Dart Frog CLI](https://pub.dev/packages/dart_frog_cli)
