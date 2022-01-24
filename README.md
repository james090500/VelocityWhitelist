
# 📃VelocityWhitelist
A Proxy wide Whitelist for Velocity

## Permissions
| Permission | Purpose |
|--|--|
| `vwhitelist.admin` | Needed for all `/vwhitelist` commands |
| `vwhitelist.bypass` | Can bypass whitelist even if not in list |

## Commands
| Command | Response |
|--|--|
| `/vwhitelist` | Info command |
| `/vwhitelist on` | Turn the whitelist on |
| `/vwhitelist off` | Turn the whitelist off |
| `/vwhitelist add <username>` | Add a user to the whitelist |
| `/vwhitelist remove <username>` | Remove a user from the whitelist |
| `/vwhitelist reload` | Reload the whitelist config |

## Config
```toml
# Whether the whitelist should be on or off
enabled = true
# The message to be shown on user disconnect
message = "&cWhitelist enabled!"
```