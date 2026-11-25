import { useState } from 'react'
import heroImage from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className="starter-page">
      <main>
        <section className="hero-section" aria-labelledby="hero-title">
          <div className="hero-graphic" aria-label="React and Vite logos">
            <img className="hero-base" src={heroImage} alt="" />
            <img className="hero-react" src={reactLogo} alt="React" />
            <img className="hero-vite" src={viteLogo} alt="Vite" />
          </div>

          <div className="hero-copy">
            <h1 id="hero-title">Get started</h1>
            <p>
              Edit <code>src/App.jsx</code> and save to test <code>HMR</code>
            </p>
          </div>

          <button
            className="counter-button"
            type="button"
            onClick={() => setCount((currentCount) => currentCount + 1)}
          >
            Count is {count}
          </button>
        </section>

        <section className="resources" aria-label="Resources and community">
          <article className="resource-card">
            <svg className="resource-icon" role="presentation" aria-hidden="true">
              <use href="/icons.svg#documentation-icon" />
            </svg>
            <h2>Documentation</h2>
            <p>Your questions, answered</p>
            <ul className="resource-links">
              <li>
                <a href="https://vite.dev/" target="_blank" rel="noreferrer">
                  <img className="vite-link-logo" src={viteLogo} alt="" />
                  Explore Vite
                </a>
              </li>
              <li>
                <a href="https://react.dev/" target="_blank" rel="noreferrer">
                  <img className="react-link-logo" src={reactLogo} alt="" />
                  Learn more
                </a>
              </li>
            </ul>
          </article>

          <article className="resource-card community-card">
            <svg className="resource-icon" role="presentation" aria-hidden="true">
              <use href="/icons.svg#social-icon" />
            </svg>
            <h2>Connect with us</h2>
            <p>Join the Vite community</p>
            <ul className="resource-links social-links">
              <li>
                <a href="https://github.com/vitejs/vite" target="_blank" rel="noreferrer">
                  <svg className="social-icon" role="presentation" aria-hidden="true">
                    <use href="/icons.svg#github-icon" />
                  </svg>
                  GitHub
                </a>
              </li>
              <li>
                <a href="https://chat.vite.dev/" target="_blank" rel="noreferrer">
                  <svg className="social-icon" role="presentation" aria-hidden="true">
                    <use href="/icons.svg#discord-icon" />
                  </svg>
                  Discord
                </a>
              </li>
              <li>
                <a href="https://x.com/vite_js" target="_blank" rel="noreferrer">
                  <svg className="social-icon" role="presentation" aria-hidden="true">
                    <use href="/icons.svg#x-icon" />
                  </svg>
                  X.com
                </a>
              </li>
              <li>
                <a href="https://bsky.app/profile/vite.dev" target="_blank" rel="noreferrer">
                  <svg className="social-icon" role="presentation" aria-hidden="true">
                    <use href="/icons.svg#bluesky-icon" />
                  </svg>
                  Bluesky
                </a>
              </li>
            </ul>
          </article>
        </section>
      </main>
      <div className="page-spacer" aria-hidden="true" />
    </div>
  )
}

export default App
