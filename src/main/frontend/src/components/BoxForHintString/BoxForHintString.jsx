import './BoxForHintString.css'
import '../../styles/boxForHint.css'
function BoxForHintString({ value, category }) {
    if (!value) return null;
    return (
        <>
            <div className="boxForHint">
                <p className="value">{value}</p>
            </div>
        </>
    );
}
export default BoxForHintString;