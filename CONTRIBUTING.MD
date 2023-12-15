# Contributing to projet2-ps-23-24-citadels-2024-b

First off, thank you for considering contributing to our Citadel project. It's people like you that make Citadel such a
great game.

## Where do I go from here?

If you've noticed a bug or have a feature request, make sure to check
our [Issues](https://github.com/pns-si3-projects/projet2-ps-23-24-citadels-2024-b/issues) if there's something similar
to what you've observed or want to suggest. If there isn't, feel free to open a new issue!

## Fork & create a branch

If this is something you think you can fix, then fork ProjetS5 and create a branch with a descriptive name.

A good branch name would be (where issue (feature) is the feature you're working on):

```bash
git switch -c feature/(feature)
```

## Get the code

The first thing you'll need to do is to fork the repository and clone it to your local machine.
See [github help page](https://help.github.com/articles/fork-a-repo) for help.

Then, there's a specific branching model for development:

1. `master`: This is where the stable code lies. It's production-ready. It's also the branch you should fork off of.
2. `dev`: This is where development happens. Pull requests should go here.
3. `feature/(feature)`: This is where you develop new features. When you're done, you should make a pull request
   to `dev`.
4. `fix/(feature)`: This is where you fix a feature. When you're done, you should make a pull request
   to `dev`.

## Code Guidelines

- Ensure any install or build dependencies are removed before the end of the layer when doing a build.
- Update the README.md with details of changes to the interface, this includes new environment variables, exposed ports,
  useful file locations and container parameters.
- You may merge the Pull Request in once you have the sign-off of two other developers, or if you do not have permission
  to do that, you may request the second reviewer to merge it for you.

## Submitting a Pull Request

Submit a pull request to the `dev` branch. Include a clear title and description.

## Code review process

The bigger the pull request, the longer it will take to review and merge. Try to break down large pull requests in
smaller chunks that are easier to review and merge.

It is also always helpful to have some context or background for the pull request. What was the purpose of the change,
why was it made, etc.

## Closing issues

If your pull request adheres to the correct naming conventions, you can automatically close issues with your commit
message or pull request body. Just include the special keyword syntax (eg. "Fixes #325") at the end of your commit
message or pull request body. More information can be
found [here](https://help.github.com/articles/closing-issues-using-keywords/).

## Questions

If you have any questions, create
an [issue](https://github.com/pns-si3-projects/projet2-ps-23-24-citadels-2024-b/issues) (protip: do a quick search first
to see if someone else didn't ask the same question before!).
